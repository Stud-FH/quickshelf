package ch.bookoflies.qsserver.controller;

import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.rest.dto.UserDTO;
import ch.bookoflies.qsserver.rest.mapper.DTOMapper;
import ch.bookoflies.qsserver.service.AuthenticationService;
import ch.bookoflies.qsserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    UserController(
            AuthenticationService authenticationService,
            UserService userService
    ) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO getUser(
            Principal principal
    ) {
        User user = authenticationService.findUser(principal);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }
    
}
