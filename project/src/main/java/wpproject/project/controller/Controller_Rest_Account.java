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
        return "hello from api";
    }

    @GetMapping("/api/database/users")
    public ResponseEntity<List<DTO_Account>> getUsers(HttpSession session) {
        List<Account> userList = serviceAccount.findAll();

        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("No session"); }
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
        return serviceAccount.findOneByUsername(username);
    }

    @GetMapping("/api/database/user/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        return serviceAccount.findOne(id);
    }

    @PostMapping("/api/user/register")
    public Account registerAccount(@RequestBody DTO_AccountRegister accountRequest, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user != null) { System.err.println("[x] already logged in"); }

        try {
            Account account = serviceAccount.findOneByMailAddress(accountRequest.getMailAddress());
            if (account != null) { System.err.println("[x] can't register, mail exists:" + accountRequest.getMailAddress()); return null; }
            account = serviceAccount.findOneByUsername(accountRequest.getUsername());
            if (account != null) { System.err.println("[x] can't register, username exists:" + accountRequest.getUsername()); return null; }

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
            System.err.println("[x] failed to register: " + e.getMessage());
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

    @PostMapping("/api/user/admin/addauthor")
    public Account admin_addAuthor(@RequestBody DTO_AccountAuthorNew DTOAccountAuthorNew, HttpSession session){

        // must be logged in
        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("[x] you have to be logged in"); return null; }

        // must be admin
        user = serviceAccount.findOne(user.getId());
        if (user.getAccountRole() != Account_Role.ADMINISTRATOR) { System.err.println("[x] not admin: " + user); return null; }

        try {
            // check if exists by mail/username
            if (serviceAccount.findOneByMailAddress(DTOAccountAuthorNew.getMailAddress()) != null) { System.err.println("[x] can't add new user (author), mail exists:"     + DTOAccountAuthorNew.getMailAddress()); return null; }
            if (serviceAccount.findOneByUsername(DTOAccountAuthorNew.getUsername()) != null)       { System.err.println("[x] can't add new user (author), username exists:" + DTOAccountAuthorNew.getUsername());    return null; }

            Account newAccount = new Account(
                DTOAccountAuthorNew.getFirstName(),
                DTOAccountAuthorNew.getLastName(),
                DTOAccountAuthorNew.getUsername(),
                DTOAccountAuthorNew.getMailAddress(),
                DTOAccountAuthorNew.getPassword()
            );

            newAccount.setAccountRole(Account_Role.AUTHOR);

            serviceAccount.save(newAccount);

            return newAccount;
        } catch (Exception e) {
            System.err.println("[x] failed to add new user (author): " + e.getMessage());
            return null;
        }
    }
}
