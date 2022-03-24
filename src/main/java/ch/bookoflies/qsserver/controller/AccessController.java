package ch.bookoflies.qsserver.controller;

import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.rest.dto.UserDTO;
import ch.bookoflies.qsserver.rest.mapper.DTOMapper;
import ch.bookoflies.qsserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccessController {

    private final UserService userService;

    AccessController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/restricted")
    public String restricted() {
        return "logged in";
    }

    /**
     * creates a new user, using the request's data as blueprint
     * @param userDTO blueprint
     * @return newly created user data
     */
    @PostMapping("/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDTO registerUser(
            @RequestBody UserDTO userDTO
    ) {
        User blueprint = DTOMapper.INSTANCE.convertUserDTOtoEntity(userDTO);
        User created = userService.createUser(blueprint);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(created);
    }

    /**
     * updates an user
     * @param userDTO blueprint
     * @param id client id
     * @param token authentication token
     * @return updated user data
     */
    @PutMapping("/user/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO updateUser(
            @RequestBody UserDTO userDTO,
            @RequestHeader("id") String id,
            @RequestHeader("token") String token
    ) {
        User blueprint = DTOMapper.INSTANCE.convertUserDTOtoEntity(userDTO);
        User user = userService.authenticateUser(id, token);
        User updated = userService.updateUser(user.getId(), blueprint);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(updated);
    }

    /**
     * deletes an user
     * @param id client id
     * @param token authentication token
     */
    @DeleteMapping("/user/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @RequestHeader("id") String id,
            @RequestHeader("token") String token
    ) {
        User user = userService.authenticateUser(id, token);
        userService.deleteUser(user.getId());
    }

    /**
     * fetches user data
     * @param id user id
     * @param token authentication token
     * @return found user data
     */
    @GetMapping("/user/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO getUser(
            @RequestHeader("id") String id,
            @RequestHeader("token") String token
    ) {
        User user = userService.authenticateUser(id, token);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }

    /**
     * similar to getUser(), except that this method uses the email address instead of the id for
     * identification, and the password instead of the token for verification.
     * can't be done remotely.
     * also refreshes the user's verification token.
     * @param name user name
     * @param password user password
     * @return user data
     */
    @GetMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO loginUser(
            @RequestHeader("name") String name,
            @RequestHeader("password") String password
    ) {
        User user = userService.loginUser(name, password);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }
    /**
     * similar to getUser(), except that this method uses the email address instead of the id for
     * identification, and the password instead of the token for verification.
     * can't be done remotely.
     * also refreshes the user's verification token.
     * @param name user name
     * @param password user password
     * @return user data
     */
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO login(
            @RequestHeader("name") String name,
            @RequestHeader("password") String password
    ) {
        User user = userService.loginUser(name, password);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }
    
}
