package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.AccountUserDTO;
import wpproject.project.dto.BookDTO;
import wpproject.project.model.AccountUser;
import wpproject.project.model.Book;
import wpproject.project.service.AccountUserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountUserRestController {
    @Autowired
    private AccountUserService accountUserService;

    @GetMapping("/api")
    public String welcome() {
        return "Hello from api";
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<AccountUserDTO>> getUsers(HttpSession session) {
        List<AccountUser> userList = accountUserService.findAll();

        AccountUser user = (AccountUser) session.getAttribute("user");
        if (user == null) {
            System.out.println("No session");
        } else {
            System.out.println(user);
        }

        List<AccountUserDTO> dtos = new ArrayList<>();
        for (AccountUser u : userList) {
            AccountUserDTO dto = new AccountUserDTO(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/user/{username}")
    public AccountUser getUser(@PathVariable(name = "username") String username, HttpSession session) {
        AccountUser user = (AccountUser) session.getAttribute("user");
        System.out.println(user);
        session.invalidate();
        return accountUserService.findOne(username);
    }

    @GetMapping("/api/user/id/{id}")
    public AccountUser getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        AccountUser user = (AccountUser) session.getAttribute("user");
        System.out.println(user);
        session.invalidate();
        return accountUserService.findOne(id);
    }
}
