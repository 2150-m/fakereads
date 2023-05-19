package WPPROJECT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import WPPROJECT.model.AccountUser;

public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

}
