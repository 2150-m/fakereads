package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.BookDTO;
import wpproject.project.dto.ShelfItemDTO;
import wpproject.project.model.*;
import wpproject.project.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class ShelfItemRestController {
    @Autowired
    private ShelfItemService shelfItemService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ShelfService shelfService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BookReviewService reviewService;

    @GetMapping("/api/database/books")
    public ResponseEntity<List<ShelfItemDTO>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = shelfItemService.findAll();

        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        if (item == null) { System.out.println("No session"); }
        else              { System.out.println(item);         }

        List<ShelfItemDTO> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) { ShelfItemDTO dto = new ShelfItemDTO(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/book/{id}")
    public ShelfItem getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return shelfItemService.findOne(id);
    }

    @GetMapping("/api/database/book/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return shelfItemService.findByBook(bookService.findOne(title));
    }

    // TODO: has a server error
    @GetMapping("/api/database/book/search={search}")
    public ResponseEntity<List<ShelfItemDTO>> searchItems(@PathVariable(name = "search") String search, HttpSession session) {
        List<ShelfItem> items = shelfItemService.findAll();

        List<ShelfItemDTO> dtos = new ArrayList<>();
        for (ShelfItem i : items) {
            // search by title / description
            if (i.getBook().getTitle().contains(search) || i.getBook().getDescription().contains(search)) {
                ShelfItemDTO dto = new ShelfItemDTO(i); dtos.add(dto);
            }
        }

        return ResponseEntity.ok(dtos);
    }

    // TODO: release date reader
    @PostMapping("/api/database/book/add")
    public ResponseEntity<String> addItem(@RequestBody BookDTO bookDTO, HttpSession session) {
        try {
            if (bookService.findByIsbn(bookDTO.getIsbn()) != null) {
                return ResponseEntity.badRequest().body("A book with the same ISBN (" + bookDTO.getIsbn() + ") is already in the database.");
            }

            Book book = new Book(bookDTO.getTitle(), bookDTO.getCoverPhoto(), LocalDate.now(), bookDTO.getDescription(), bookDTO.getNumOfPages(), 0, bookDTO.getIsbn());
            bookService.save(book);

            ShelfItem item = new ShelfItem(book);
            shelfItemService.save(item);

            return ResponseEntity.ok(book.getTitle().toUpperCase() + " (" + book.getId() + ") has been added to the database.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the item: " + e.getMessage());
        }
    }

    @PostMapping("/api/user/add/book/{bookId}/shelf/{shelfName}")
    public ResponseEntity<String> userAddBookID(@PathVariable(name = "bookId") Long bookId, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = accountService.findOne(user.getId());

        ShelfItem targetItem = shelfItemService.findByBook(bookService.findOne(bookId));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        return userAddBook(user, targetItem, shelfName);
    }

    @PostMapping("/api/user/add/book/isbn={isbn}/shelf/{shelfName}")
    public ResponseEntity<String> userAddBookISBN(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = accountService.findOne(user.getId());

        ShelfItem targetItem = shelfItemService.findByBook(bookService.findByIsbn(isbn));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        return userAddBook(user, targetItem, shelfName);
    }

    private ResponseEntity<String> userAddBook(Account user, ShelfItem targetItem, String shelfName) {
        Shelf targetShelf = null;
        for (Shelf s : user.getShelves()) {
            if (s.getName().equalsIgnoreCase(shelfName)) {
                targetShelf = s;
                break;
            }
        }

        if (targetShelf == null) {
            return ResponseEntity.badRequest().body("Shelf " + shelfName.toUpperCase() + " does not exist.");
        }

        // contains() and object.equals(otherobject) don't work
        for (ShelfItem item : targetShelf.getShelfItems()) {
            if (item.getId().equals(targetItem.getId())) {
                return ResponseEntity.badRequest().body("This book is already on " + targetShelf.getName().toUpperCase() + ".");
            }
        }

        if (targetShelf.isPrimary()) {
            // Remove the book from the other primary shelf (if it's on it)
            outer:
            for (Shelf shelf : user.getShelves().subList(0, 3)) {
                if (shelf.getId().equals(targetShelf.getId())) { continue; }

                Iterator<ShelfItem> iterator = shelf.getShelfItems().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId().equals(targetItem.getId())) {
                        iterator.remove();
                        break outer;
                    }
                }
            }

            targetShelf.getShelfItems().add(targetItem);
            shelfService.save(targetShelf);

            String msg = targetItem.getBook().getTitle().toUpperCase() + " (" + targetItem.getBook().getId() + ") has been added to " + targetShelf.getName().toUpperCase() + " (" + targetShelf.getId() + ").";

            if (targetShelf.getName().equalsIgnoreCase("Read")) {
                msg += addReview(user, targetItem);
            }

            return ResponseEntity.ok().body(msg);
        }

        // Check if the item is in a primary shelf
        for (Shelf shelf : user.getShelves().subList(0, 3)) {
            for (ShelfItem item : shelf.getShelfItems()) {
                if (item.getId().equals(targetItem.getId())) {
                    targetShelf.getShelfItems().add(targetItem);
                    shelfService.save(targetShelf);
                    return ResponseEntity.ok().body(targetItem.getBook().getTitle().toUpperCase() + "(" + targetItem.getBook().getId() + ") has been added to " + targetShelf.getName().toUpperCase() + " (" + targetShelf.getId() + ").");
                }
            }
        }

        return ResponseEntity.badRequest().body("An item has to be on a primary shelf in order to be added to custom ones.");
    }

    // TODO new reviews update ratings

    private String addReview(Account user, ShelfItem item) {
        BookReview review = new BookReview(7, "review text", LocalDate.now(), user);
        reviewService.save(review);

        item.getBookReviews().add(review);
        shelfItemService.save(item);

        return "\nReview posted.";
    }

    // TODO: independent review system

    @PostMapping("/api/user/remove/book/{bookId}/shelf/name={shelfName}")
    public ResponseEntity<String> userRemoveBookID(@PathVariable(name = "bookId") Long bookID, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }

        return userRemoveBook(accountService.findOne(user.getId()), bookService.findOne(bookID), shelfName);
    }

    @PostMapping("/api/user/remove/book/isbn={isbn}/shelf/name={shelfName}")
    public ResponseEntity<String> userRemoveBookISBN(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }

        return userRemoveBook(accountService.findOne(user.getId()), bookService.findByIsbn(isbn), shelfName);
    }

    private ResponseEntity<String> userRemoveBook(Account user, Book book, String shelfName) {
        ShelfItem item = null;

        // Find the shelf
        outer:
        for (Shelf s : user.getShelves()) {
            if (!s.getName().equalsIgnoreCase(shelfName)) { continue; }

            Iterator<ShelfItem> iterator = s.getShelfItems().iterator();
            while (iterator.hasNext()) {
                ShelfItem i = iterator.next();
                if (i.getBook().getId().equals(book.getId())) {
                    item = i;
                    break outer;
                }
            }
        }

        if (item == null) { return ResponseEntity.badRequest().body("Could not find a shelf called: " + shelfName.toUpperCase()); }

        String msg = "";
        boolean inPrimary = false;

        outer: // Check if it's on a primary shelf
        for (Shelf s : user.getShelves().subList(0, 3)) {
            Iterator<ShelfItem> iterator = s.getShelfItems().iterator();
            while (iterator.hasNext()) {
                ShelfItem i = iterator.next();
                if (i.getId().equals(item.getId())) {
                    msg += i.getBook().getTitle().toUpperCase() + " (" + i.getBook().getId() + ") has been removed from " + s.getName().toUpperCase() + ".\n";
                    inPrimary = true;
                    iterator.remove();
                    shelfService.save(user.getShelves());

                    // Remove the associated review
                    if (shelfName.equalsIgnoreCase("Read")) {
                        msg += removeReview(user, i, book);
                    }

                    break outer;
                }
            }
        }

        for (Shelf s : user.getShelves().subList(3, user.getShelves().size())) {
            Iterator<ShelfItem> iterator = s.getShelfItems().iterator();
            while (iterator.hasNext()) {
                ShelfItem i = iterator.next();
                if (i.getId().equals(item.getId())) {
                    msg += i.getBook().getTitle().toUpperCase() + " (" + i.getBook().getId() + ") has been removed from " + s.getName().toUpperCase() + ".\n";

                    iterator.remove();
                    shelfService.save(user.getShelves());

                    if (!inPrimary) { return ResponseEntity.ok(msg); }
                }
            }
        }

        if (inPrimary) { return ResponseEntity.ok(msg); }
        return ResponseEntity.badRequest().body("Could not find " + item.getBook().getTitle().toUpperCase() + " (" + item.getBook().getId() + ") in " + shelfName.toUpperCase() + ".");
    }

    private String removeReview(Account user, ShelfItem item, Book book) {
        Iterator<BookReview> iterator = item.getBookReviews().iterator();
        while (iterator.hasNext()) {
            BookReview r = iterator.next();
            if (item.getBook().getId().equals(book.getId()) && r.getAccount() != null && r.getAccount().getId().equals(user.getId())) {
                iterator.remove();
                reviewService.saveAll(item.getBookReviews());
                shelfItemService.save(item);

                return "Review (" + r.getId() + "), made by " + r.getAccount().getUsername().toUpperCase() + ", of " + book.getTitle().toUpperCase() + " (" + book.getId() + ") has been removed.\n";
            }
        }

        return "Could not find an associated review.";
    }
}
