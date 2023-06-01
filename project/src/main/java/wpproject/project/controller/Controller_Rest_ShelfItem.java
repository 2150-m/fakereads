package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.DTO_Book;
import wpproject.project.dto.DTO_BookReviewNew;
import wpproject.project.dto.DTO_ShelfItem;
import wpproject.project.model.*;
import wpproject.project.service.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class Controller_Rest_ShelfItem {
    @Autowired
    private Service_ShelfItem serviceShelfItem;
    @Autowired
    private Service_Book serviceBook;
    @Autowired
    private Service_Shelf serviceShelf;
    @Autowired
    private Service_Account serviceAccount;
    @Autowired
    private Service_BookReview reviewService;

    @GetMapping("/api/database/books")
    public ResponseEntity<List<DTO_ShelfItem>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = serviceShelfItem.findAll();

        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        if (item == null) { System.out.println("No session"); }
        else              { System.out.println(item);         }

        List<DTO_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) { DTO_ShelfItem dto = new DTO_ShelfItem(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/book/{id}")
    public ShelfItem getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return serviceShelfItem.findOne(id);
    }

    @GetMapping("/api/database/book/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return serviceShelfItem.findByBook(serviceBook.findOne(title));
    }

    // TODO: has a server error
    @GetMapping("/api/database/book/search={search}")
    public ResponseEntity<List<DTO_ShelfItem>> searchItems(@PathVariable(name = "search") String search, HttpSession session) {
        List<ShelfItem> items = serviceShelfItem.findAll();

        List<DTO_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem i : items) {
            // search by title / description
            if (i.getBook().getTitle().contains(search) || i.getBook().getDescription().contains(search)) {
                DTO_ShelfItem dto = new DTO_ShelfItem(i); dtos.add(dto);
            }
        }

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/api/database/book/add")
    public ResponseEntity<String> addItem(@RequestBody DTO_Book DTOBook, HttpSession session) {
        try {
            if (serviceBook.findByIsbn(DTOBook.getIsbn()) != null) {
                return ResponseEntity.badRequest().body("A book with the same ISBN (" + DTOBook.getIsbn() + ") is already in the database.");
            }

            Book book = new Book(DTOBook.getTitle(), DTOBook.getCoverPhoto(), LocalDate.now(), DTOBook.getDescription(), DTOBook.getNumOfPages(), 0, DTOBook.getIsbn());
            serviceBook.save(book);

            ShelfItem item = new ShelfItem(book);
            serviceShelfItem.save(item);

            return ResponseEntity.ok(book.getTitle().toUpperCase() + " (" + book.getId() + ") has been added to the database.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the item: " + e.getMessage());
        }
    }

    // TODO: ask

    @PostMapping("/api/user/add/book/{bookId}/shelf/name={shelfName}")
    public ResponseEntity<String> userAddBookID(@PathVariable(name = "bookId") Long bookId, @PathVariable(name = "shelfName") String shelfName, @RequestBody(required = false) DTO_BookReviewNew review, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = serviceAccount.findOne(user.getId());

        ShelfItem targetItem = serviceShelfItem.findByBook(serviceBook.findOne(bookId));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        return userAddBook(user, targetItem, shelfName, review);
    }

    @PostMapping("/api/user/add/book/isbn={isbn}/shelf/name={shelfName}")
    public ResponseEntity<String> userAddBookISBN(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, @RequestBody(required = false) DTO_BookReviewNew review, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = serviceAccount.findOne(user.getId());

        ShelfItem targetItem = serviceShelfItem.findByBook(serviceBook.findByIsbn(isbn));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        return userAddBook(user, targetItem, shelfName, review);
    }

    // TODO: add to multiple shelves in a single move

    private ResponseEntity<String> userAddBook(Account user, ShelfItem targetItem, String shelfName, DTO_BookReviewNew review) {
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
            serviceShelf.save(targetShelf);

            String msg = targetItem.getBook().getTitle().toUpperCase() + " (" + targetItem.getBook().getId() + ") has been added to " + targetShelf.getName().toUpperCase() + " (" + targetShelf.getId() + ").";

            if (targetShelf.getName().equalsIgnoreCase("Read") && review != null) {
                    msg += addReview(user, targetItem, review);
            }

            return ResponseEntity.ok().body(msg);
        }

        // Check if the item is in a primary shelf
        for (Shelf shelf : user.getShelves().subList(0, 3)) {
            for (ShelfItem item : shelf.getShelfItems()) {
                if (item.getId().equals(targetItem.getId())) {
                    targetShelf.getShelfItems().add(targetItem);
                    serviceShelf.save(targetShelf);
                    return ResponseEntity.ok().body(targetItem.getBook().getTitle().toUpperCase() + "(" + targetItem.getBook().getId() + ") has been added to " + targetShelf.getName().toUpperCase() + " (" + targetShelf.getId() + ").");
                }
            }
        }

        return ResponseEntity.badRequest().body("An item has to be on a primary shelf in order to be added to custom ones.");
    }

    private String addReview(Account user, ShelfItem item, DTO_BookReviewNew review) {
        BookReview newReview = new BookReview(review.getRating(), review.getText(), LocalDate.now(), user);
        reviewService.save(newReview);
        item.getBookReviews().add(newReview);

        double sum = 0;
        int counter = 0;
        for (BookReview r : item.getBookReviews()) {
            sum += r.getRating();
            counter++;
        }

        item.getBook().setRating(new BigDecimal(sum / counter).setScale(2, RoundingMode.HALF_UP).doubleValue());
        serviceBook.save(item.getBook());
        serviceShelfItem.save(item);
        return "\nReview posted.";
    }

    @PostMapping("/api/user/remove/book/{bookId}/shelf/name={shelfName}")
    public ResponseEntity<String> userRemoveBookID(@PathVariable(name = "bookId") Long bookID, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }

        return userRemoveBook(serviceAccount.findOne(user.getId()), serviceBook.findOne(bookID), shelfName);
    }

    @PostMapping("/api/user/remove/book/isbn={isbn}/shelf/name={shelfName}")
    public ResponseEntity<String> userRemoveBookISBN(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }

        return userRemoveBook(serviceAccount.findOne(user.getId()), serviceBook.findByIsbn(isbn), shelfName);
    }

    private ResponseEntity<String> userRemoveBook(Account user, Book book, String shelfName) {
        boolean doesExist = false;
        boolean inPrimaryShelf = false;
        for (Shelf s : user.getShelves()) {
            if (!s.getName().equalsIgnoreCase(shelfName)) { continue; }

            doesExist = true;
            if (s.isPrimary()) { inPrimaryShelf = true; }
            break;
        }
        if (!doesExist) { return ResponseEntity.badRequest().body("Could not find " + shelfName.toUpperCase() + " in this user's shelf list."); }

        String msg = "";
        ShelfItem targetItem = serviceShelfItem.findByBook(book);

        if (!(shelfName.equalsIgnoreCase("read") || shelfName.equalsIgnoreCase("currentlyreading") || shelfName.equalsIgnoreCase("wanttoread"))) {
            for (Shelf s : user.getShelves().subList(3, user.getShelves().size())) {
                if (!s.getName().equalsIgnoreCase(shelfName)) { continue; }

                Iterator<ShelfItem> iterator = s.getShelfItems().iterator();
                while (iterator.hasNext()) {
                    ShelfItem i = iterator.next();
                    if (i.getId().equals(targetItem.getId())) {
                        msg += i.getBook().getTitle().toUpperCase() + " (" + i.getBook().getId() + ") has been removed from " + s.getName().toUpperCase() + ".\n";

                        iterator.remove();
                        serviceShelf.save(user.getShelves());

                        // If not in a primary, the only thing that needs to be removed is a single ShelfItem
                        if (!inPrimaryShelf) { return ResponseEntity.ok(msg); }
                    }
                }
            }
        }

        for (Shelf s : user.getShelves().subList(0, 3)) {
            if (!s.getName().equalsIgnoreCase(shelfName)) { continue; }

            Iterator<ShelfItem> iterator = s.getShelfItems().iterator();
            while (iterator.hasNext()) {
                ShelfItem i = iterator.next();
                if (i.getId().equals(targetItem.getId())) {
                    msg += i.getBook().getTitle().toUpperCase() + " (" + i.getBook().getId() + ") has been removed from " + s.getName().toUpperCase() + ".\n";

                    // Remove the associated review
                    if (shelfName.equalsIgnoreCase("Read")) {
                        msg += removeReview(user, i, book);
                    }

                    iterator.remove();
                    serviceShelf.save(user.getShelves());

                    return ResponseEntity.ok(msg);
                }
            }
        }

        return ResponseEntity.badRequest().body("Could not find " + book.getTitle().toUpperCase() + " (" + book.getId() + ") in " + shelfName.toUpperCase() + ".");
    }

    private String removeReview(Account user, ShelfItem item, Book book) {
        Iterator<BookReview> iterator = item.getBookReviews().iterator();
        while (iterator.hasNext()) {
            BookReview r = iterator.next();
            if (item.getBook().getId().equals(book.getId()) && r.getAccount() != null && r.getAccount().getId().equals(user.getId())) {
                iterator.remove();
                reviewService.saveAll(item.getBookReviews());
                serviceShelfItem.save(item);

                return "Review (" + r.getId() + "), made by " + r.getAccount().getUsername().toUpperCase() + ", of " + book.getTitle().toUpperCase() + " (" + book.getId() + ") has been removed.\n";
            }
        }

        return "Could not find an associated review.";
    }
}
