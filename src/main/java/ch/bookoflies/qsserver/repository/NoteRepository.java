package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("noteRepository")
public interface NoteRepository extends JpaRepository<Note, String> {

}
