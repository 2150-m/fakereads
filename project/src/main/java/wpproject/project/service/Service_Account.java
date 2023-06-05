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
    private Repository_Account repositoryAccount;

    public Account findOneByUsername(String username) {
        Optional<Account> account = repositoryAccount.findByUsername(username);
        return account.orElse(null);
    }

    public Account findOneByMailAddress(String mailAddress) {
        Optional<Account> account = repositoryAccount.findByMailAddress(mailAddress);
        return account.orElse(null);
    }

    public Account findOne(Long id) {
        Optional<Account> account = repositoryAccount.findById(id);
        return account.orElse(null);
    }

    public List<Account> findAll() {
        return repositoryAccount.findAll();
    }

    public Account save(Account account) {
        return repositoryAccount.save(account);
    }

    public Account login(String username, String password) {
        Account account = repositoryAccount.getByUsername(username);
        if(account == null || !account.getPassword().equals(password)) { return null; }
        return  account;
    }
}
