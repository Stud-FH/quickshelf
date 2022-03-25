package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.name = :name")
    Optional<User> findByName(String name);

    User findByAuthenticationIdentitiesContains(String identity);
}
