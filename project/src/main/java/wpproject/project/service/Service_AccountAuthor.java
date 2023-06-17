package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.AccountAuthor;
import wpproject.project.repository.Repository_Account;
import wpproject.project.repository.Repository_AccountAuthor;
import java.util.List;

@Service
public class Service_AccountAuthor extends Service_Account {

    @Autowired
    private Repository_AccountAuthor repositoryAccountAuthor;

    @Autowired
    private Repository_Account repositoryAccount;

    //#
    //# ESSENTIAL
    //#

    public AccountAuthor findOne(Long id) { return repositoryAccountAuthor.findById(id).orElse(null); }
    public List<AccountAuthor> findAllAuthors() {
        return repositoryAccountAuthor.findAll();
    }
    public AccountAuthor save(AccountAuthor author) {
        repositoryAccount.save(author);
        return repositoryAccountAuthor.save(author);
    }

    public AccountAuthor findById(Long id) { return repositoryAccountAuthor.findById(id).orElse(null); }
    public AccountAuthor findOneByUsername(String username) { return repositoryAccountAuthor.findByUsername(username).orElse(null); }

    //#
    //# FUNCTIONAL
    //#


}
