package wpproject.project.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.AccountDTO;
import wpproject.project.dto.AccountLoginDTO;
import wpproject.project.dto.AccountRegisterDTO;
import wpproject.project.model.*;
import wpproject.project.service.AccountService;
import wpproject.project.service.BookService;
import wpproject.project.service.ShelfItemService;
import wpproject.project.service.ShelfService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@RestController
public class AccountRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ShelfService shelfService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ShelfItemService shelfItemService;

    @GetMapping("/api")
    public String welcome() {
        return "Hello from api";
    }

    @GetMapping("/api/database/users")
    public ResponseEntity<List<AccountDTO>> getUsers(HttpSession session) {
        List<Account> userList = accountService.findAll();

        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.out.println("No session"); }
        else              { System.out.println(user);         }

        List<AccountDTO> dtos = new ArrayList<>();
        for (Account u : userList) {
            AccountDTO dto = new AccountDTO(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/user/username={username}")
    public Account getUser(@PathVariable(name = "username") String username, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
//        System.out.println(user);
//        session.invalidate();
        return accountService.findOneByUsername(username);
    }

    @GetMapping("/api/database/user/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
//        System.out.println(user);
//        session.invalidate();
        return accountService.findOne(id);
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<String> registerAccount(@RequestBody AccountRegisterDTO accountRequest, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return ResponseEntity.badRequest().body("Already logged in"); }

        try {
            Account account = accountService.findOneByMailAddress(accountRequest.getMailAddress());
            if (account != null) { return ResponseEntity.badRequest().body("User with this mail address already exists."); }
            account = accountService.findOneByUsername(accountRequest.getUsername());
            if (account != null) { return ResponseEntity.badRequest().body("User with this username already exists."); }

            account = new Account(accountRequest.getFirstName(), accountRequest.getLastName(), accountRequest.getUsername(), accountRequest.getMailAddress(), accountRequest.getPassword());

            Shelf shelf_WantToRead = new Shelf("WantToRead", true);
            Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
            Shelf shelf_Read = new Shelf("Read", true);
            shelfService.save(shelf_WantToRead);
            shelfService.save(shelf_CurrentlyReading);
            shelfService.save(shelf_Read);
            account.setShelves(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));

            accountService.save(account);

            session.setAttribute("user", account);
            return ResponseEntity.ok("Succesfully registered: " + account.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register: " + e.getMessage());
        }
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<String> login(@RequestBody AccountLoginDTO accountLoginDTO, HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return ResponseEntity.badRequest().body("Already logged in"); }

        if(accountLoginDTO.getUsername().isEmpty() || accountLoginDTO.getPassword().isEmpty())  { return new ResponseEntity("Invalid login data", HttpStatus.BAD_REQUEST); }

        Account loggedAccount = accountService.login(accountLoginDTO.getUsername(), accountLoginDTO.getPassword());
        if (loggedAccount == null)  { return new ResponseEntity<>("Account does not exist!", HttpStatus.NOT_FOUND); }

        session.setAttribute("user", loggedAccount);
        return ResponseEntity.ok("Successfully logged in: " + loggedAccount.getUsername());
    }

    @GetMapping("/api/user/myaccount")
    public Account myaccount(HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return null; }
        return accountService.findOneByUsername(user.getUsername());
    }

    @PostMapping("/api/user/logout")
    public ResponseEntity logout(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("Can't log out: already logged out."); }

        session.invalidate();
        return ResponseEntity.ok().body("Succesfully logged out: " + user.getUsername());
    }
}
