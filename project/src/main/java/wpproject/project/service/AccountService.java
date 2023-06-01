package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.dto.AccountDTO;
import wpproject.project.model.Account;
import wpproject.project.model.Shelf;
import wpproject.project.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account findOneByUsername(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.orElse(null);
    }

    public Account findOneByMailAddress(String mailAddress) {
        Optional<Account> account = accountRepository.findByMailAddress(mailAddress);
        return account.orElse(null);
    }

    public Account findOne(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElse(null);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        Account account = accountRepository.getByUsername(username);
        if(account == null || !account.getPassword().equals(password)) { return null; }
        return  account;
    }
}
