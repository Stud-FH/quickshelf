package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("libraryRepository")
public interface LibraryRepository extends JpaRepository<Library, String> {

    @Query("SELECT l FROM Library l WHERE l.name = :name")
    Optional<Library> findByName(String name);
}
