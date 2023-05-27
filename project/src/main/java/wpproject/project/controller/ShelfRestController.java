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

@RestController
public class ShelfRestController {
    @Autowired
    private ShelfService shelfService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/database/user/{userId}/shelf/{shelfId}")
    public Shelf getUserShelf(@PathVariable(name = "userId") Long userID, @PathVariable(name = "shelfId") Long shelfID, HttpSession session) {
        Account user = accountService.findOne(userID);
        if (user == null) { return null; }

        Shelf shelf = shelfService.findOne(shelfID);
        if (shelf == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(shelf.getId())) {
                return shelf;
            }
        }

        return null;
    }

    @PostMapping("/api/user/add/shelf")
    public ResponseEntity addShelf(@RequestBody String newShelfName, HttpSession session) {
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

            return ResponseEntity.ok("A new shelf had been added.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the shelf: " + e.getMessage());
        }
    }
}
