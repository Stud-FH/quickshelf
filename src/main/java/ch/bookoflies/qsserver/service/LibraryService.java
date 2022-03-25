package ch.bookoflies.qsserver.service;

import ch.bookoflies.qsserver.persistent.Library;
import ch.bookoflies.qsserver.persistent.User;
import ch.bookoflies.qsserver.repository.LibraryRepository;
import ch.bookoflies.qsserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class LibraryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryService.class);

    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;

    public LibraryService(
            @Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("libraryRepository") LibraryRepository libraryRepository
    ) {
        this.userRepository = userRepository;
        this.libraryRepository = libraryRepository;
    }

    /**
     * creates a library
     * @param blueprint a blueprint of the library to create
     * @return the created entity
     * @throws ResponseStatusException if any attribute violates any security- or integrity constraints.
     */
    public Library createLibrary(Library blueprint, User creator) {
        blueprint.setId(null);
        blueprint.setOwner(creator);
        Library created = libraryRepository.saveAndFlush(blueprint);
        logLibraryActivity(created, "created");
        return created;
    }

    private void logLibraryActivity(Library library, String activity) {
        LOGGER.info(String.format("%s(%s): %s.", library.getId(), library.getName(), activity));
    }
}
