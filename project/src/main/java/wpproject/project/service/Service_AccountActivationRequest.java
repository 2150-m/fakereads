package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.AccountActivationRequest;
import wpproject.project.repository.Repository_AccountActivationRequest;

import java.util.List;

@Service
public class Service_AccountActivationRequest {

    @Autowired
    private Repository_AccountActivationRequest repositoryAccountActivationRequest;

    public AccountActivationRequest save(AccountActivationRequest accountActivationRequest) {
        return repositoryAccountActivationRequest.save(accountActivationRequest);
    }

    public List<AccountActivationRequest> findAll() {
        return repositoryAccountActivationRequest.findAll();
    }
}