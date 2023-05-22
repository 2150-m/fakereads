package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Account;
import wpproject.project.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account findOne(String username) {
        Optional<Account> foundUser = accountRepository.findByUsername(username);
        return foundUser.orElse(null);
    }

    public Account findOne(Long id) {
        Optional<Account> foundUser = accountRepository.findById(id);
        return foundUser.orElse(null);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
