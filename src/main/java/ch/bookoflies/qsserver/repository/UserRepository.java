package ch.bookoflies.qsserver.repository;

import ch.bookoflies.qsserver.persistent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByName(String name);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    Optional<User> findByName(String name);

    @Query("select u from User u join u.authenticationIdentities a where a = ?1")
    User findByAuthenticationIdentitiesContains(String identity);
}
