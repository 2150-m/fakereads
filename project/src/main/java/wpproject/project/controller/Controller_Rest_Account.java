package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.*;
import wpproject.project.model.*;
import wpproject.project.service.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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
    @Autowired
    private Service_BookReview serviceBookReview;
    @Autowired
    private Service_BookReview reviewService;

    @PostMapping("/api/user/register")
    public Account registerAccount(@RequestBody DTO_AccountRegister accountRequest, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user != null) { System.err.println("[x] already logged in"); return null; }

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
        return serviceAccount.findOne(user.getId());
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

    @PutMapping("/api/user/update/review/book_id={id}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "id") Long bookId, @RequestBody DTO_BookReviewNew newReviewDTO, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

        for (ShelfItem i : user.getShelves().get(2).getShelfItems()) {
            if (!i.getBook().getId().equals(bookId)) { continue; }

            // Update an existing review made by this user
            for (BookReview r : i.getBookReviews()) {
                if (r.getAccount() != null && !r.getAccount().getId().equals(user.getId())) { continue; }

                r.setRating(newReviewDTO.getRating());
                r.setText(newReviewDTO.getText());

                serviceBookReview.save(r);
                return ResponseEntity.ok("Review updated.");
            }

            // A review made by this user doesn't exist
            return addNewReview(user, i, newReviewDTO);
        }

        return ResponseEntity.badRequest().body("Review update failed.");
    }

    @PutMapping("/api/user/update/review/book_isbn={isbn}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "isbn") String isbn, @RequestBody DTO_BookReviewNew newReviewDTO, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

        for (ShelfItem i : user.getShelves().get(2).getShelfItems()) {
            if (!i.getBook().getIsbn().equals(isbn)) { continue; }

            // Update an existing review made by this user
            for (BookReview r : i.getBookReviews()) {
                if (r.getAccount() != null && !r.getAccount().getId().equals(user.getId())) { continue; }

                r.setRating(newReviewDTO.getRating());
                r.setText(newReviewDTO.getText());

                serviceBookReview.save(r);
                return ResponseEntity.ok("Review updated.");
            }

            // A review made by this user doesn't exist
            return addNewReview(user, i, newReviewDTO);
        }

        return ResponseEntity.badRequest().body("Could not find a book with the ISBN " + isbn + " in this user's account.");
    }

    private ResponseEntity<String> addNewReview(Account user, ShelfItem i, DTO_BookReviewNew newReviewDTO) {
        BookReview newReview = new BookReview(newReviewDTO.getRating(), newReviewDTO.getText(), LocalDate.now(), user);
        i.getBookReviews().add(newReview);
        serviceBookReview.save(newReview);
        return ResponseEntity.ok("Review added.");
    }

    @PostMapping("/api/user/add/shelf")
    public ResponseEntity<String> addShelf(@RequestBody String newShelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

        try {
            for (Shelf shelf : user.getShelves()) {
                if (shelf.getName().equals(newShelfName)) {
                    return ResponseEntity.badRequest().body("This user already has a shelf with this name.");
                }
            }

            Shelf newShelf = new Shelf(newShelfName, false);
            serviceShelf.save(newShelf);

            user.getShelves().add(newShelf);
            serviceAccount.save(user);

            return ResponseEntity.ok("A new shelf, " + newShelfName.toUpperCase() + " (" + newShelf.getId() + "), had been added.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the shelf: " + e.getMessage());
        }
    }

    @PostMapping("/api/user/remove/shelf/{id}")
    public ResponseEntity<String> removeShelfID(@PathVariable(name = "id") Long id, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

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
        user = serviceAccount.findOne(user.getId());

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

            serviceShelf.save(user.getShelves());
            return ResponseEntity.ok(shelf.getName().toUpperCase() + " (" + shelf.getId() + ") has been removed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not remove the shelf" + e.getMessage());
        }
    }

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
