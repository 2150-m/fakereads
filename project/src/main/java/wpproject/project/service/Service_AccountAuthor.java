package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Account;
import wpproject.project.model.AccountAuthor;
import wpproject.project.repository.Repository_Account;
import wpproject.project.repository.Repository_AccountAuthor;

import java.util.Optional;

@Service
public class Service_AccountAuthor {
    @Autowired
    private Repository_AccountAuthor repositoryAccountAuthor;
    @Autowired
    private Repository_Account repositoryAccount;

    public AccountAuthor save(AccountAuthor author) {
        return repositoryAccountAuthor.save(author);
    }

    public AccountAuthor findById(Long id) {
        Optional<AccountAuthor> account = repositoryAccountAuthor.findById(id);
        return account.orElse(null);
    }

    public AccountAuthor findOneByUsername(String username) {
        Optional<AccountAuthor> account = repositoryAccountAuthor.findByUsername(username);
        return account.orElse(null);
    }
}
