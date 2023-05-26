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

    @GetMapping("/api/users")
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

    @GetMapping("/api/user/username={username}")
    public Account getUser(@PathVariable(name = "username") String username, HttpSession session) {
//        Account user = (Account) session.getAttribute("user");
//        System.out.println(user);
//        session.invalidate();
        return accountService.findOneByUsername(username);
    }

    @GetMapping("/api/user/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
//        Account user = (Account) session.getAttribute("user");
//        System.out.println(user);
//        session.invalidate();
        return accountService.findOne(id);
    }

    @PostMapping("/api/register")
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

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody AccountLoginDTO accountLoginDTO, HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return ResponseEntity.badRequest().body("Already logged in"); }

        if(accountLoginDTO.getUsername().isEmpty() || accountLoginDTO.getPassword().isEmpty())  { return new ResponseEntity("Invalid login data", HttpStatus.BAD_REQUEST); }

        Account loggedAccount = accountService.login(accountLoginDTO.getUsername(), accountLoginDTO.getPassword());
        if (loggedAccount == null)  { return new ResponseEntity<>("Account does not exist!", HttpStatus.NOT_FOUND); }

        session.setAttribute("user", loggedAccount);
        return ResponseEntity.ok("Successfully logged in: " + loggedAccount.getUsername());
    }

    @GetMapping("/api/myaccount")
    public Account myaccount(HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return null; }
        return accountService.findOneByUsername(user.getUsername());
    }

    @PostMapping("/api/logout")
    public ResponseEntity logout(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("Can't log out: already logged out."); }

        session.invalidate();
        return ResponseEntity.ok().body("Succesfully logged out: " + user.getUsername());
    }

    @PostMapping("/api/user/add/book_id={bookId}/shelf={shelfName}")
    public ResponseEntity userAddBook(@PathVariable(name = "bookId") Long bookId, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in in order to add a book to a shelf.");
        }

        ShelfItem targetItem = shelfItemService.findByBook(bookService.findOne(bookId));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        Shelf targetShelf = null;
        for (Shelf userShelf : user.getShelves()) {
            if (userShelf.getName().equalsIgnoreCase(shelfName)) {
                targetShelf = userShelf;
                break;
            }
        }

        if (targetShelf == null) {
            return ResponseEntity.badRequest().body("Shelf [" + shelfName + "] does not exist.");
        }

        // contains() and object.equals(otherobject) don't work
        for (ShelfItem item : targetShelf.getShelfItems()) {
            if (item.getId().equals(targetItem.getId())) {
                return ResponseEntity.badRequest().body("This item/book is already on '" + targetShelf.getName() + "'.");
            }
        }

        if (targetShelf.isPrimary()) {
            // Remove the book from the other primary shelf (if it's on it)
            for (Shelf shelf : user.getShelves().subList(0, 3)) {
                if (shelf.getId().equals(targetShelf.getId())) { continue; }

                Iterator<ShelfItem> iterator = shelf.getShelfItems().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId().equals(targetItem.getId())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            targetShelf.getShelfItems().add(targetItem);
            shelfService.save(targetShelf);
            return ResponseEntity.ok().body("[id: " + targetItem.getBook().getId() + "  / title: " + targetItem.getBook().getTitle() + "] has been added to " + targetShelf.getName() + " (id: " + targetShelf.getId() + ").");
        }
//        else if (shelfService.findOne(shelfName) != null) {
//            targetShelf.getShelfItems().add(targetItem);
//            shelfService.save(targetShelf);
//            return ResponseEntity.ok().body("[id: " + targetItem.getBook().getId() + "  / title: " + targetItem.getBook().getTitle() + "] has been added to [" + shelfName + "].");
//        }

        return ResponseEntity.badRequest().body("An item has to be in a primary shelf in order to be added to custom ones.");
    }

//    private List<Shelf> DefaultShelves() {
//        Shelf shelf_WantToRead = new Shelf("WantToRead", true);
//        Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
//        Shelf shelf_Read = new Shelf("Read", true);
//        return List.of(shelf_Read, shelf_CurrentlyReading, shelf_WantToRead);
//    }
}
