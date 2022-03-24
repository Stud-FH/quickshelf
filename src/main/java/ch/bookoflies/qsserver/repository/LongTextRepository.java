package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.LongText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("longTextRepository")
public interface LongTextRepository extends JpaRepository<LongText, String> {

}
