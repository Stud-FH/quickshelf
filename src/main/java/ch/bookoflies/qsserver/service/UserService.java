package ch.bookoflies.qsserver.service;

import ch.bookoflies.qsserver.util.CustomOAuth2User;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class UserService extends DefaultOAuth2UserService {

    private static final long HASH_INITIAL = 1000099; // used to hash passwords. Modifying this number will invalidate all existing passwords.
    private static final long HASH_MULTIPLIER = 31;   // used to hash passwords. Modifying this number will invalidate all existing passwords.

    private static final long MIN_PASSWORD_LENGTH = 8;
    private static final long MAX_PASSWORD_LENGTH = 20;
    private static final long MIN_PASSWORD_CHARACTER_DIVERSITY = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(
            @Qualifier("userRepository") UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }


    public void processOAuthPostLogin(String name) {
        User user = userRepository.findByName(name).orElse(null);

        if (user == null) {
            user = new User();
            user.setName(name);
            user = createUser(user);
        }
        logUserActivity(user, "logged in through oauth");
        LOGGER.info(user.getToken());
    }

    /**
     * verifies an user id by the corresponding token.
     * The admin token will verify any user id.
     * @param id the user id that is tried to access
     * @param token the verification token
     * @return the user corresponding to the given id
     * @throws ResponseStatusException
     *      404 if no user with this id exists;
     *      401 if token does not match the user and is not the admin token;
     *      403 if the minimum clearance level is not met;
     */
    public User authenticateUser(String id, String token) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid user id"));

        if (!user.getToken().equals(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");

        return user;
    }

    /**
     * creates an user
     * @param blueprint a blueprint of the user to create
     * @return the new user
     * @throws ResponseStatusException if any attribute violates any security- or integrity constraints.
     */
    public User createUser(User blueprint) {
        blueprint.setId(null);
//        checkEmailFormat(blueprint.getEmail());
//        checkEmailUniqueness(blueprint.getEmail());

//        checkPasswordFormat(blueprint.getPassword()); // TODO

        blueprint.setPasswordHash(hash(blueprint.getPassword()));
        blueprint.setPassword(null);
        blueprint.setToken(UUID.randomUUID().toString());

        // TODO verify user by email (requires additional attribute verificationToken, REST interface and mail sender)

        User user = userRepository.saveAndFlush(blueprint);
        logUserActivity(user, "created");
        return user;
    }

    /**
     * fetches the user information from the repository through the email address.
     * also refreshes the user's authentication token.
     * @param name user name
     * @param password user password
     * @return the user
     * @throws ResponseStatusException 404 if no user with this name exists; 401 if the password is wrong
     */
    public User loginUser(String name, String password) {

        User user = userRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown user name"));

        Long passwordHash = hash(password);
        if (!passwordHash.equals(user.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong password");

        // refresh token
        user.setToken(UUID.randomUUID().toString());

        userRepository.flush();
        logUserActivity(user, "logged in");
        return user;
    }

    /**
     * updates an user.
     * null attributes in the blueprint are ignored
     * @param id the id of the user to update
     * @param blueprint attributes to update
     * @return the updated user
     * @throws ResponseStatusException if an update violates any security- or integrity constraints. In that case no updates are performed.
     */
    public User updateUser(String id, User blueprint) {
        if (!userRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown id");

        User user = userRepository.getById(id);

//        // first check all modifications
//        if (blueprint.getEmail() != null && !blueprint.getEmail().equals(user.getEmail())) {
//            checkEmailFormat(blueprint.getEmail());
//            checkEmailUniqueness(blueprint.getEmail());
//        }
        if (blueprint.getPassword() != null && !blueprint.getPassword().equals(user.getPassword())) {
            checkPasswordFormat(blueprint.getPassword());
        }

//        // if all modifications are valid, perform the updates
//        if (blueprint.getEmail() != null && !blueprint.getEmail().equals(user.getEmail())) {
//            user.setEmail(blueprint.getEmail());
//        }
        if (blueprint.getPassword() != null && !blueprint.getPassword().equals(user.getPassword())) {
            user.setPasswordHash(hash(blueprint.getPassword()));
        }
        userRepository.flush();
        logUserActivity(user, "updated");
        return user;
    }

    /**
     * deletes an user
     * @param id the id of the user to delete
     * @throws ResponseStatusException 404 if there is no such id; 403 if this user is an admin
     */
    public void deleteUser(String id) {
        if (!userRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown id");
        User user = userRepository.getById(id);
        userRepository.deleteById(id);
        userRepository.flush();
        logUserActivity(user, "deleted");
    }

    /**
     * hashes a String to a long
     * @param str the String to hash
     * @return  hashed String (long)
     */
    private long hash(String str) {
        if (str == null) return 0;
        long h = HASH_INITIAL;
        for (char c : str.toCharArray()) h = HASH_MULTIPLIER * h + c;
        return h;
    }

    /**
     * checks whether a String is a valid email address.
     * Currently not checking anything.
     * @param email the String to check
     */
    private void checkEmailFormat(String email) {
        if (email == null || email.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "email cannot be empty");
        /*
         * could check for valid email format here,
         * but user verification through email would solve the problem of invalid emails anyway
         */
    }

    /**
     * checks whether a String is a valid password.
     * @param password the String to check
     * @throws ResponseStatusException 422 if any of the following requirements is not met:
     *      - not null
     *      - length within MIN_PASSWORD_LENGTH and MAX_PASSWORD_LENGTH
     *      - consists only of letters, digits and allowed special characters
     *      - sufficient character diversity
     */
    private void checkPasswordFormat(String password) {
        if (password == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "password cannot be null");
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("password length must be within %d and %d characters.", MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));

        int lowercase = 0;
        int uppercase = 0;
        int digit = 0;
        int special = 0;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) lowercase++;
            else if (Character.isUpperCase(c)) uppercase++;
            else if (Character.isDigit(c)) digit++;
            else  special++;
        }
        if (Math.min(lowercase, 1) + Math.min(uppercase, 1) + Math.min(digit, 1) + Math.min(special, 1) < MIN_PASSWORD_CHARACTER_DIVERSITY)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("password must contain at least %d different types of characters", MIN_PASSWORD_CHARACTER_DIVERSITY));

    }

    private void logUserActivity(User user, String activity) {
        LOGGER.info(String.format("%s(%s): %s.", user.getId(), user.getName(), activity));
    }
}
