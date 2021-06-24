package fh.server.repository;

import fh.server.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);
    Account getByEmail(String name);
}
