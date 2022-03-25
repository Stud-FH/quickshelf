package ch.bookoflies.qsserver.controller;

import ch.bookoflies.qsserver.persistent.Library;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.rest.dto.LibraryDTO;
import ch.bookoflies.qsserver.rest.mapper.DTOMapper;
import ch.bookoflies.qsserver.service.AuthenticationService;
import ch.bookoflies.qsserver.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class LibraryController {

    private final AuthenticationService authenticationService;
    private final LibraryService libraryService;

    LibraryController(
            AuthenticationService authenticationService,
            LibraryService libraryService
    ) {
        this.authenticationService = authenticationService;
        this.libraryService = libraryService;
    }



    /**
     * creates a new library, using the request's data as blueprint
     * @param libraryDTO blueprint
     * @return newly created user data
     */
    @PostMapping("/library/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LibraryDTO createLibrary(
            Principal principal,
            @RequestBody LibraryDTO libraryDTO
    ) {
        Library blueprint = DTOMapper.INSTANCE.convertLibraryDTOtoEntity(libraryDTO);
        User user = authenticationService.findUser(principal);
        Library created = libraryService.createLibrary(blueprint, user);
        return DTOMapper.INSTANCE.convertEntityToLibraryDTO(created);
    }

    /**
     *
     */
    @GetMapping("/foobar")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LibraryDTO foobar(
            Principal principal
    ) {
        Library blueprint = new Library();
        User user = authenticationService.findUser(principal);
        Library created = libraryService.createLibrary(blueprint, user);
        return DTOMapper.INSTANCE.convertEntityToLibraryDTO(created);
    }


}
