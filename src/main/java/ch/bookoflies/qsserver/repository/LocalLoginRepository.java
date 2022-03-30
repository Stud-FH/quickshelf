package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.LocalLogin;
import ch.bookoflies.qsserver.persistent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("localLoginRepository")
public interface LocalLoginRepository extends JpaRepository<LocalLogin, String> {

    Optional<LocalLogin> findById(String id);
}
