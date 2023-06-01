package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Account;

import java.util.Optional;

public interface Repository_Account extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByMailAddress(String mail);
    Account getByUsername(String username);
}
