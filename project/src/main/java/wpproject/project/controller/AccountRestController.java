package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.AccountDTO;
import wpproject.project.model.Account;
import wpproject.project.service.AccountService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountRestController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/api")
    public String welcome() {
        return "Hello from api";
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<AccountDTO>> getUsers(HttpSession session) {
        List<Account> userList = accountService.findAll();

        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            System.out.println("No session");
        } else {
            System.out.println(user);
        }

        List<AccountDTO> dtos = new ArrayList<>();
        for (Account u : userList) {
            AccountDTO dto = new AccountDTO(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/user/name={username}")
    public Account getUser(@PathVariable(name = "username") String username, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        System.out.println(user);
        session.invalidate();
        return accountService.findOne(username);
    }

    @GetMapping("/api/user/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        System.out.println(user);
        session.invalidate();
        return accountService.findOne(id);
    }
}
