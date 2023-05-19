package WPPROJECT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import WPPROJECT.model.AccountActivationRequest;

public interface AccountActivationRequestRepository extends JpaRepository<AccountActivationRequest, Long> {
}
