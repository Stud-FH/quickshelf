package ch.bookoflies.qsserver.service;

import ch.bookoflies.qsserver.persistent.LocalLogin;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.repository.LocalLoginRepository;
import ch.bookoflies.qsserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
public class AuthenticationService extends DefaultOAuth2UserService {

    private static final long MIN_PASSWORD_LENGTH = 8;
    private static final long MAX_PASSWORD_LENGTH = 20;
    private static final long MIN_PASSWORD_CHARACTER_DIVERSITY = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository;
    private final LocalLoginRepository localLoginRepository;

    private final UserService userService;

    public AuthenticationService(
            @Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("localLoginRepository") LocalLoginRepository localLoginRepository,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.localLoginRepository = localLoginRepository;
        this.userService = userService;
    }


    public User findUser(Principal principal) {
        return userRepository.findByAuthenticationIdentitiesContains(principal.getName());
    }


    public void processOAuthPostLogin(AuthenticatedPrincipal principal) {
        String name = principal.getName();
        User user = userRepository.findByAuthenticationIdentitiesContains(name);

        if (user == null) {
            user = new User();
            user.addAuthenticationIdentity(name);
            user = userService.createUser(user);
        }
        logUserActivity(user, "logged in in via oauth2 as "+name);
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
