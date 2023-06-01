package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.model.Account;
import wpproject.project.model.Shelf;
import wpproject.project.service.AccountService;
import wpproject.project.service.ShelfService;

import java.util.Iterator;
import java.util.List;

@RestController
public class Controller_Rest_Shelf {
    @Autowired
    private ShelfService shelfService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/database/user/{userId}/shelf/{shelfId}")
    public Shelf getUserShelf(@PathVariable(name = "userId") Long userID, @PathVariable(name = "shelfId") Long shelfID, HttpSession session) {
        Account user = accountService.findOne(userID);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(shelfID)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/database/user/{userId}/shelf/name={shelfName}")
    public Shelf getUserShelf(@PathVariable(name = "userId") Long userID, @PathVariable(name = "shelfName") String shelfname, HttpSession session) {
        Account user = accountService.findOne(userID);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getName().equalsIgnoreCase(shelfname)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/database/user/username={userName}/shelf/{shelfId}")
    public Shelf getUserShelf(@PathVariable(name = "userName") String username, @PathVariable(name = "shelfId") Long shelfID, HttpSession session) {
        Account user = accountService.findOneByUsername(username);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(shelfID)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/database/user/username={userName}/shelf/name={shelfName}")
    public Shelf getUserShelf(@PathVariable(name = "userName") String username, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = accountService.findOneByUsername(username);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getName().equalsIgnoreCase(shelfName)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/database/user/{userId}/shelves")
    public List<Shelf> getUserShelves(@PathVariable(name = "userId") Long userID, HttpSession session) {
        Account user = accountService.findOne(userID);
        if (user == null) { return null; }

        return user.getShelves();
    }

    @GetMapping("/api/database/user/username={userName}/shelves")
    public List<Shelf> getUserShelves(@PathVariable(name = "userName") String username, HttpSession session) {
        Account user = accountService.findOneByUsername(username);
        if (user == null) { return null; }

        return user.getShelves();
    }

    @PostMapping("/api/user/add/shelf")
    public ResponseEntity<String> addShelf(@RequestBody String newShelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = accountService.findOne(user.getId());

        try {
            for (Shelf shelf : user.getShelves()) {
                if (shelf.getName().equals(newShelfName)) {
                    return ResponseEntity.badRequest().body("This user already has a shelf with this name.");
                }
            }

            Shelf newShelf = new Shelf(newShelfName, false);
            shelfService.save(newShelf);

            user.getShelves().add(newShelf);
            accountService.save(user);

            return ResponseEntity.ok("A new shelf, " + newShelfName.toUpperCase() + " (" + newShelf.getId() + "), had been added.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the shelf: " + e.getMessage());
        }
    }

    @PostMapping("/api/user/remove/shelf/{id}")
    public ResponseEntity<String> removeShelfID(@PathVariable(name = "id") Long id, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = accountService.findOne(user.getId());

        Shelf shelf = null;
        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(id)) {
                shelf = s;
                break;
            }
        }
        return removeShelf(user, shelf);
    }

    @PostMapping("/api/user/remove/shelf/name={name}")
    public ResponseEntity<String> removeShelfName(@PathVariable(name = "name") String name, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = accountService.findOne(user.getId());

        Shelf shelf = null;
        for (Shelf s : user.getShelves()) {
            if (s.getName().equalsIgnoreCase(name)) {
                shelf = s;
                break;
            }
        }
        return removeShelf(user, shelf);
    }

    private ResponseEntity<String> removeShelf(Account user, Shelf shelf) {
        if (shelf == null) { return ResponseEntity.badRequest().body("This shelf does not exist."); }
        if (shelf.isPrimary()) { return ResponseEntity.badRequest().body("Primary shelves can not be removed."); }

        try {
            Iterator<Shelf> iterator = user.getShelves().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId().equals(shelf.getId())) {
                    iterator.remove();
                    break;
                }
            }

            shelfService.save(user.getShelves());
            return ResponseEntity.ok(shelf.getName().toUpperCase() + " (" + shelf.getId() + ") has been removed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not remove the shelf" + e.getMessage());
        }
    }
}
