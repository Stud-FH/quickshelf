package ch.bookoflies.qsserver.service;

import ch.bookoflies.qsserver.persistent.LocalLogin;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.repository.LocalLoginRepository;
import ch.bookoflies.qsserver.repository.UserRepository;
import ch.bookoflies.qsserver.util.CustomOAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.UUID;

@Service
public class AuthenticationService extends DefaultOAuth2UserService {

    private static final long MIN_PASSWORD_LENGTH = 8;
    private static final long MAX_PASSWORD_LENGTH = 20;
    private static final long MIN_PASSWORD_CHARACTER_DIVERSITY = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository;
    private final LocalLoginRepository localLoginRepository;

    public AuthenticationService(
            @Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("localLoginRepository") LocalLoginRepository localLoginRepository
    ) {
        this.userRepository = userRepository;
        this.localLoginRepository = localLoginRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        return new CustomOAuth2User(user); // TODO try without
    }


    public User getUser(Principal principal) {
        System.out.println(principal.getName());
        return userRepository.findByAuthenticationIdentitiesContains(principal.getName());
    }


    public void processOAuthPostLogin(Principal principal) {
        String name = principal.getName();
        User user = userRepository.findByName(name).orElse(null);

        if (user == null) {
            user = new User();
            user.addAuthenticationIdentity(name);
            user = createUser(user);
        }
        logUserActivity(user, "logged in in via oauth2");
        LOGGER.info(user.getToken()); // TODO debugging
    }

    /**
     * verifies a user id by the corresponding token.
     * The admin token will verify any user id.
     * @param id the user id that is tried to access
     * @param token the verification token
     * @return the user corresponding to the given id
     * @throws ResponseStatusException
     *      404 if no user with this id exists;
     *      401 if token does not match the user and is not the admin token;
     */
    public User authenticateUser(String id, String token) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid user id"));

        if (!user.getToken().equals(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");

        return user;
    }

    /**
     * creates a user
     * @param blueprint a blueprint of the user to create
     * @return the new user
     * @throws ResponseStatusException if any attribute violates any security- or integrity constraints.
     */
    public User createUser(User blueprint) {
        blueprint.setId(null);
//        checkEmailFormat(blueprint.getEmail());
//        checkEmailUniqueness(blueprint.getEmail());

        blueprint.setToken(UUID.randomUUID().toString());

        // TODO verify user by email (requires additional attribute verificationToken, REST interface and mail sender)

        User user = userRepository.saveAndFlush(blueprint);
        logUserActivity(user, "created");
        return user;
    }

    /**
     * creates a local login for a user.
     * does not check user existence! should never be called if user existence not granted.
     * @param user the user to create a local login for
     * @param password raw password
     * @return created entity
     */
    public LocalLogin createLocalLogin(User user, String password) {
        LocalLogin localLogin = new LocalLogin();
        localLogin.setUser(user);
        localLogin.setPasswordEncoding(passwordEncoder.encode(password));

        localLogin = localLoginRepository.saveAndFlush(localLogin);
        logUserActivity(user, "created a local login");
        return localLogin;
    }

    /**
     * fetches the user information from the repository through the email address.
     * also refreshes the user's authentication token.
     * @return the user
     * @throws ResponseStatusException
     *      404 if no user with this name exists;
     *      401 if the password is wrong;
     */
    public User processLocalLogin(String username, String password) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown user name"));
        LocalLogin localLogin = localLoginRepository.findByUser(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no local login registered for this user"));
        if (!passwordEncoder.matches(password, localLogin.getPasswordEncoding()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong password");

        logUserActivity(user, "logged in via local login");
        return user;
    }

    private void logUserActivity(User user, String activity) {
        LOGGER.info(String.format("%s(%s): %s.", user.getId(), user.getName(), activity));
    }
}
