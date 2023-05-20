package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.AccountUser;

public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

}
