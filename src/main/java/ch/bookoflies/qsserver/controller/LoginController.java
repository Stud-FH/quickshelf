package ch.bookoflies.qsserver.controller;

import ch.bookoflies.qsserver.persistent.LocalLogin;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.rest.dto.LibraryDTO;
import ch.bookoflies.qsserver.rest.dto.LoginDTO;
import ch.bookoflies.qsserver.rest.dto.UserDTO;
import ch.bookoflies.qsserver.rest.mapper.DTOMapper;
import ch.bookoflies.qsserver.service.AuthenticationService;
import ch.bookoflies.qsserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private final AuthenticationService authenticationService;

    LoginController(
            AuthenticationService authenticationService
            ) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO register(
            @RequestBody LoginDTO data
    ) {
        LocalLogin login = DTOMapper.INSTANCE.convertLoginDTOtoEntity(data);
        User user = authenticationService.register(login);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO localLogin(
            @RequestBody LoginDTO data
    ) {
        LocalLogin login = DTOMapper.INSTANCE.convertLoginDTOtoEntity(data);
        User user = authenticationService.processLocalLogin(login);
        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @RequestHeader("token") String token
    ) {
        authenticationService.logout(token);
    }
    
}
