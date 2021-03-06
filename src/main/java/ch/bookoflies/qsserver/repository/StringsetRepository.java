package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.Stringset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("stringsetRepository")
public interface StringsetRepository extends JpaRepository<Stringset, String> {

}
