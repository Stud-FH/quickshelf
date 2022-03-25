package ch.bookoflies.qsserver.service;

import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(
            @Qualifier("userRepository") UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    /**
     * creates a user
     * @param blueprint a blueprint of the user to create
     * @return the new user
     * @throws ResponseStatusException if any attribute violates any security- or integrity constraints.
     */
    public User createUser(User blueprint) {
        blueprint.setId(null);

        blueprint.setToken(UUID.randomUUID().toString());

        User user = userRepository.saveAndFlush(blueprint);
        logUserActivity(user, "created");
        return user;
    }

    /**
     * updates a user.
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

//        // if all modifications are valid, perform the updates
//        if (blueprint.getEmail() != null && !blueprint.getEmail().equals(user.getEmail())) {
//            user.setEmail(blueprint.getEmail());
//        }
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

    private void logUserActivity(User user, String activity) {
        LOGGER.info(String.format("%s(%s): %s.", user.getId(), user.getName(), activity));
    }
}
