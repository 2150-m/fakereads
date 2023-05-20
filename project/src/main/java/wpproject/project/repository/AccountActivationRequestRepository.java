package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.AccountActivationRequest;

public interface AccountActivationRequestRepository extends JpaRepository<AccountActivationRequest, Long> {
}
