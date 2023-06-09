package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Account;
import wpproject.project.model.AccountAuthor;

import java.util.Optional;

public interface Repository_AccountAuthor extends JpaRepository<AccountAuthor, Long> {
    Optional<AccountAuthor> findByUsername(String username);
}
