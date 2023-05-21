package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.AccountUser;

import java.util.Optional;

public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
    Optional<AccountUser> findByUsername(String username);
}
