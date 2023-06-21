package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import wpproject.project.model.Account;
import wpproject.project.repository.Repository_Account;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class Service_Account {
    @Autowired
    protected Repository_Account repositoryAccount;

    //#
    //# ESSENTIAL
    //#

    public Account findOneByUsername(String username) { return repositoryAccount.findByUsername(username).orElse(null); }
    public Account findOneByMailAddress(String mailAddress) { return repositoryAccount.findByMailAddress(mailAddress).orElse(null); }
    public Account findOne(Long id) { return repositoryAccount.findById(id).orElse(null); }
    public List<Account> findAll() {
        return repositoryAccount.findAll();
    }
    public Account save(Account account) {
        return repositoryAccount.save(account);
    }

    //#
    //# FUNCTIONAL
    //#
}
