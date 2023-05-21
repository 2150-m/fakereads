package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.AccountUser;
import wpproject.project.repository.AccountUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountUserService {
    @Autowired
    private AccountUserRepository accountUserRepository;

    public AccountUser findOne(Long id) {
        Optional<AccountUser> foundUser = accountUserRepository.findById(id);
        return foundUser.orElse(null);
    }

    public List<AccountUser> findAll() {
        return accountUserRepository.findAll();
    }

    public AccountUser save(AccountUser accountUser) {
        return accountUserRepository.save(accountUser);
    }
}
