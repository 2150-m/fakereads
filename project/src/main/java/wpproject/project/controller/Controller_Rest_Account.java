package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.*;
import wpproject.project.model.*;
import wpproject.project.service.Service_Account;
import wpproject.project.service.Service_Book;
import wpproject.project.service.Service_ShelfItem;
import wpproject.project.service.Service_Shelf;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller_Rest_Account {
    @Autowired
    private Service_Account serviceAccount;
    @Autowired
    private Service_Shelf serviceShelf;
    @Autowired
    private Service_Book serviceBook;
    @Autowired
    private Service_ShelfItem serviceShelfItem;

    @GetMapping("/api")
    public String welcome() {
        return "Hello from api";
    }

    @GetMapping("/api/database/users")
    public ResponseEntity<List<DTO_Account>> getUsers(HttpSession session) {
        List<Account> userList = serviceAccount.findAll();

        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.out.println("No session"); }
        else              { System.out.println(user);         }

        List<DTO_Account> dtos = new ArrayList<>();
        for (Account u : userList) {
            DTO_Account dto = new DTO_Account(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/user/username={username}")
    public Account getUser(@PathVariable(name = "username") String username, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
//        System.out.println(user);
//        session.invalidate();
        user = serviceAccount.findOne(user.getId());
        return user;
    }

    @GetMapping("/api/database/user/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
//        System.out.println(user);
//        session.invalidate();
        user = serviceAccount.findOne(user.getId());
        return user;
    }

    @PostMapping("/api/user/register")
    public Account registerAccount(@RequestBody DTO_AccountRegister accountRequest, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user != null) { System.out.println("Already logged in"); }

        try {
            Account account = serviceAccount.findOneByMailAddress(accountRequest.getMailAddress());
            if (account != null) { System.out.println("[x] Mail exists:" + accountRequest.getUsername()); return null; }
            account = serviceAccount.findOneByUsername(accountRequest.getUsername());
            if (account != null) { System.out.println("[x] Username exists:" + accountRequest.getUsername()); return null; }

            account = new Account(accountRequest.getFirstName(), accountRequest.getLastName(), accountRequest.getUsername(), accountRequest.getMailAddress(), accountRequest.getPassword());

            Shelf shelf_WantToRead = new Shelf("WantToRead", true);
            Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
            Shelf shelf_Read = new Shelf("Read", true);
            serviceShelf.save(shelf_WantToRead);
            serviceShelf.save(shelf_CurrentlyReading);
            serviceShelf.save(shelf_Read);
            account.setShelves(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));

            serviceAccount.save(account);

            session.setAttribute("user", account);
            return account;
        } catch (Exception e) {
            System.out.println("Failed to register: " + e.getMessage());
            return null;
        }
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<String> login(@RequestBody DTO_AccountLogin DTOAccountLogin, HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return ResponseEntity.badRequest().body("Already logged in"); }

        if(DTOAccountLogin.getUsername().isEmpty() || DTOAccountLogin.getPassword().isEmpty())  { return new ResponseEntity("Invalid login data", HttpStatus.BAD_REQUEST); }

        Account loggedAccount = serviceAccount.login(DTOAccountLogin.getUsername(), DTOAccountLogin.getPassword());
        if (loggedAccount == null)  { return new ResponseEntity<>("Account does not exist!", HttpStatus.NOT_FOUND); }

        session.setAttribute("user", loggedAccount);
        return ResponseEntity.ok("Successfully logged in: " + loggedAccount.getUsername());
    }

    @GetMapping("/api/user/myaccount")
    public Account myaccount(HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return null; }
        user = serviceAccount.findOne(user.getId());
        return user;
    }

    @PostMapping("/api/user/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("Can't log out: already logged out."); }

        session.invalidate();
        return ResponseEntity.ok().body("Succesfully logged out: " + user.getUsername());
    }

    @PutMapping("/api/user/myaccount/update")
    public ResponseEntity<String> updateUser(@RequestBody DTO_AccountUpdate newInfo, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

        if (!newInfo.getFirstName().isEmpty() && !newInfo.getFirstName().equals(user.getFirstName()))                user.setFirstName(newInfo.getFirstName());
        if (!newInfo.getLastName().isEmpty() && !newInfo.getLastName().equals(user.getLastName()))                   user.setLastName(newInfo.getLastName());
        if (!newInfo.getUsername().isEmpty() && !newInfo.getUsername().equals(user.getUsername()))                   user.setUsername(newInfo.getUsername());
        if (newInfo.getDateOfBirth() != null && !newInfo.getDateOfBirth().equals(user.getDateOfBirth()))             user.setDateOfBirth(newInfo.getDateOfBirth());
        if (!newInfo.getDescription().isEmpty() && !newInfo.getDescription().equals(user.getDescription()))          user.setDescription(newInfo.getDescription());
        if (!newInfo.getProfilePicture().isEmpty() && !newInfo.getProfilePicture().equals(user.getProfilePicture())) user.setProfilePicture(newInfo.getProfilePicture());

        serviceAccount.save(user);
        return ResponseEntity.ok("User info updated.");
    }

    @PutMapping("/api/user/myaccount/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody DTO_AccountUpdatePass newInfo, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

        if (!newInfo.getMail().isEmpty()     && !newInfo.getMail().equals(user.getMailAddress()))  user.setMailAddress(newInfo.getMail());
        if (!newInfo.getPassword().isEmpty() && !newInfo.getPassword().equals(user.getPassword())) user.setPassword(newInfo.getPassword());

        serviceAccount.save(user);
        return ResponseEntity.ok("User info updated.");
    }

    @PutMapping("/api/user/myaccount/update/mail")
    public ResponseEntity<String> updateMail(@RequestBody DTO_AccountUpdatePass newInfo, HttpSession session) {
        return updatePassword(newInfo, session);
    }


    // TODO: add admin stuff

}
