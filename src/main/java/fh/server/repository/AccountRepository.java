package fh.server.repository;

import fh.server.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByName(String name);
}
