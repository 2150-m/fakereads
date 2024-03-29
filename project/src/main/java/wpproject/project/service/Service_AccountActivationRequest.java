package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.AccountActivationRequest;
import wpproject.project.model.AccountAuthor;
import wpproject.project.repository.Repository_AccountActivationRequest;

import java.util.List;
import java.util.Optional;

@Service
public class Service_AccountActivationRequest {

    @Autowired
    private Repository_AccountActivationRequest repositoryAccountActivationRequest;

    //#
    //# ESSENTIAL
    //#

    public AccountActivationRequest save(AccountActivationRequest accountActivationRequest) { return repositoryAccountActivationRequest.save(accountActivationRequest); }
    public AccountActivationRequest findById(Long id) { return repositoryAccountActivationRequest.findById(id).orElse(null); }
    public List<AccountActivationRequest> findAll() {
        return repositoryAccountActivationRequest.findAll();
    }

    //#
    //# FUNCTIONAL
    //#


}