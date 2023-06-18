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
public class Controller_Rest {
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
    @Autowired
    private Service_AccountAuthor serviceAccountAuthor;
    @Autowired
    private Service_BookGenre serviceBookGenre;
    @Autowired
    private Service_AccountActivationRequest serviceAccountActivationRequest;


    // TODO: change urls
    // TODO: rename function name by url
    // TODO: rasporediti svaki kurac u odredjeni service
    // TODO: stavi da svi kurcevi returnaju response entity nema type

    @PostMapping("/api/myaccount/register")
    public Account registerAccount(@RequestBody DTO_Post_AccountRegister accountRequest, HttpSession session) {
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

    @PostMapping("/api/myaccount/login")
    public ResponseEntity<String> login(@RequestBody DTO_Post_AccountLogin DTOAccountLogin, HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return ResponseEntity.badRequest().body("Already logged in"); }

        if (DTOAccountLogin.getUsername().isEmpty() || DTOAccountLogin.getPassword().isEmpty())  { return ResponseEntity.badRequest().body("Invalid login data"); }

        Account loggedAccount = serviceAccount.login(DTOAccountLogin.getUsername(), DTOAccountLogin.getPassword());
        if (loggedAccount == null)  { return new ResponseEntity<>("Account does not exist!", HttpStatus.NOT_FOUND); }

        user = serviceAccount.findOneByUsername(DTOAccountLogin.getUsername());
        if (user != null && user.getAccountRole() == Account_Role.AUTHOR) {
            if (!serviceAccountAuthor.findById(user.getId()).isAccountActivated()) { return ResponseEntity.badRequest().body("This author account is not activated."); }
        }

        session.setAttribute("user", loggedAccount);
        return ResponseEntity.ok("Successfully logged in: " + loggedAccount.getUsername());
    }

    @GetMapping("/api/myaccount")
    public Account myaccount(HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return null; }
        return serviceAccount.findOne(user.getId());
    }

    @PostMapping("/api/myaccount/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("Can't log out: already logged out."); }

        session.invalidate();
        return ResponseEntity.ok().body("Succesfully logged out: " + user.getUsername());
    }

    @PutMapping("/api/myaccount/update")
    public ResponseEntity<String> updateUser(@RequestBody DTO_Post_AccountUpdate newInfo, HttpSession session) {
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

    @PutMapping("/api/myaccount/update/passormail")
    public ResponseEntity<String> updatePassword(@RequestBody DTO_Post_AccountUpdatePass newInfo, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = serviceAccount.findOne(user.getId());

        if (!newInfo.getMail().isEmpty()     && !newInfo.getMail().equals(user.getMailAddress()))  user.setMailAddress(newInfo.getMail());
        if (!newInfo.getPassword().isEmpty() && !newInfo.getPassword().equals(user.getPassword())) user.setPassword(newInfo.getPassword());

        serviceAccount.save(user);
        return ResponseEntity.ok("User info updated.");
    }


    @PutMapping("/api/myaccount/update/review/book_id={id}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "id") Long bookId, @RequestBody DTO_Post_BookReview newReviewDTO, HttpSession session) {
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

    @PutMapping("/api/myaccount/update/review/book_isbn={isbn}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "isbn") String isbn, @RequestBody DTO_Post_BookReview newReviewDTO, HttpSession session) {
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

    private ResponseEntity<String> addNewReview(Account user, ShelfItem i, DTO_Post_BookReview newReviewDTO) {
        BookReview newReview = new BookReview(newReviewDTO.getRating(), newReviewDTO.getText(), LocalDate.now(), user);
        i.getBookReviews().add(newReview);
        serviceBookReview.save(newReview);
        return ResponseEntity.ok("Review added.");
    }

    @PostMapping("/api/myaccount/shelves/add")
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

    @PostMapping("/api/myaccount/shelves/remove/{id}")
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

    @PostMapping("/api/myaccount/shelves/remove/name={name}")
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

    @PostMapping("/api/myaccount/shelves/name={shelfName}/addbook/{bookId}")
    public ResponseEntity<String> userAddBookID(@PathVariable(name = "bookId") Long bookId, @PathVariable(name = "shelfName") String shelfName, @RequestBody(required = false) DTO_Post_BookReview review, HttpSession session) {
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

    @PostMapping("/api/myaccount/shelves/name={shelfName}/addbook/isbn={isbn}")
    public ResponseEntity<String> userAddBookISBN(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, @RequestBody(required = false) DTO_Post_BookReview review, HttpSession session) {
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

    private ResponseEntity<String> userAddBook(Account user, ShelfItem targetItem, String shelfName, DTO_Post_BookReview review) {
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

    private String addReview(Account user, ShelfItem item, DTO_Post_BookReview review) {
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

    @PostMapping("/api/myaccount/shelves/name={shelfName}/removebook/{bookId}")
    public ResponseEntity<String> userRemoveBookID(@PathVariable(name = "bookId") Long bookID, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }

        return userRemoveBook(serviceAccount.findOne(user.getId()), serviceBook.findOne(bookID), shelfName);
    }

    @PostMapping("/api/myaccount/shelves/name={shelfName}/removebook/isbn={isbn}")
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


    private Boolean isAdmin(HttpSession session) {
        // must be logged in
        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("[x] you have to be logged in"); return false; }

        // must be admin
        user = serviceAccount.findOne(user.getId());
        if (user.getAccountRole() != Account_Role.ADMINISTRATOR) { System.err.println("[x] not admin: " + user); return false; }

        return true;
    }

    @PostMapping("/api/admin/addauthor")
    public AccountAuthor admin_addAuthor(@RequestBody DTO_Post_AccountAuthor DTOAccountAuthorNew, HttpSession session){
        if (!isAdmin(session)) { return null; }

        try {
            // check if exists by mail/username
            if (serviceAccount.findOneByMailAddress(DTOAccountAuthorNew.getMailAddress()) != null) { System.err.println("[x] can't add new user (author), mail exists:"     + DTOAccountAuthorNew.getMailAddress()); return null; }
            if (serviceAccount.findOneByUsername(DTOAccountAuthorNew.getUsername()) != null)       { System.err.println("[x] can't add new user (author), username exists:" + DTOAccountAuthorNew.getUsername());    return null; }

            AccountAuthor newAuthor = new AccountAuthor(new Account(
                    DTOAccountAuthorNew.getFirstName(),
                    DTOAccountAuthorNew.getLastName(),
                    DTOAccountAuthorNew.getUsername(),
                    DTOAccountAuthorNew.getMailAddress(),
                    DTOAccountAuthorNew.getPassword()
            ));

            newAuthor.setAccountRole(Account_Role.AUTHOR);

            Shelf shelf_WantToRead = new Shelf("WantToRead", true);
            Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
            Shelf shelf_Read = new Shelf("Read", true);
            serviceShelf.save(shelf_WantToRead);
            serviceShelf.save(shelf_CurrentlyReading);
            serviceShelf.save(shelf_Read);
            newAuthor.getShelves().addAll(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));

//            serviceAccount.save(newAuthor);
            serviceAccountAuthor.save(newAuthor);
            return newAuthor;
        } catch (Exception e) {
            System.err.println("[x] failed to add new user (author): " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/api/admin/activations")
    public ResponseEntity<List<DTO_View_AccountActivationRequest>> getActivationRequests(HttpSession session) {
        if (!isAdmin(session)) { return null; }

        List<AccountActivationRequest> userList = serviceAccountActivationRequest.findAll();

        List<DTO_View_AccountActivationRequest> dtos = new ArrayList<>();
        for (AccountActivationRequest u : userList) {
            DTO_View_AccountActivationRequest dto = new DTO_View_AccountActivationRequest(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/api/admin/activation/{id}/accept")
    public ResponseEntity<String> acceptActivationRequest(@PathVariable(name = "id") Long id, @RequestBody DTO_Post_MailMessage dtoPostMailMessage, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        AccountActivationRequest aar = serviceAccountActivationRequest.findById(id);
        if (aar == null) { return ResponseEntity.badRequest().body("request doesn't exist"); }

        // save as approved
        aar.setStatus(AccountActivationRequest_Status.APPROVED);
        serviceAccountActivationRequest.save(aar);

        // set is activated for author
        AccountAuthor accountAuthor = aar.getAuthor();
        accountAuthor.setAccountActivated(true);
        serviceAccountAuthor.save(accountAuthor);

        // TODO: send mail that account has been activated --- send password
        System.out.println("sent mail message from admin: " + dtoPostMailMessage.getMessage());

        return ResponseEntity.ok().body("accepted activation request");
    }

    @PostMapping("/api/admin/activation/{id}/reject")
    public ResponseEntity<String> rejectActivationRequest(@PathVariable(name = "id") Long id, @RequestBody DTO_Post_MailMessage dtoPostMailMessage, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        AccountActivationRequest aar = serviceAccountActivationRequest.findById(id);
        if (aar == null) { return ResponseEntity.badRequest().body("request doesn't exist"); }

        // save as rejected
        aar.setStatus(AccountActivationRequest_Status.REJECTED);
        serviceAccountActivationRequest.save(aar);

        // set account as not activated
        AccountAuthor accountAuthor = aar.getAuthor();
        accountAuthor.setAccountActivated(false);
        serviceAccountAuthor.save(accountAuthor);

        // TODO: send mail -> reason for rejection
        System.out.println("sent mail message from admin: " + dtoPostMailMessage.getMessage());

        return ResponseEntity.ok().body("rejected activation request");
    }

    @PostMapping("/api/admin/addgenre")
    public ResponseEntity<String> addGenre(@RequestBody DTO_Post_BookGenre dtoPostBookGenre, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        try {
            BookGenre bookGenre = serviceBookGenre.findOne(dtoPostBookGenre.getName());
            if (bookGenre != null) { return ResponseEntity.badRequest().body("book genre exists"); }

            BookGenre genre = new BookGenre(dtoPostBookGenre.getName());
            serviceBookGenre.save(genre);

            return ResponseEntity.ok().body("added new genre");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("failed to add genre: " + e.getMessage());
        }
    }

    @PostMapping("/api/admin/additem")
    public ResponseEntity<String> addItemAdmin(@RequestBody DTO_Post_Book DTOBook, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        try {
            if (serviceBook.findByIsbn(DTOBook.getIsbn()) != null) {
                return ResponseEntity.badRequest().body("A book with the same ISBN (" + DTOBook.getIsbn() + ") is already in the database.");
            }

            Book book = new Book(DTOBook.getTitle(), DTOBook.getCoverPhoto(), LocalDate.now(), DTOBook.getDescription(), DTOBook.getNumOfPages(), 0, DTOBook.getIsbn());
            serviceBook.save(book);

            ShelfItem item = new ShelfItem(book);
            serviceShelfItem.save(item);

            return ResponseEntity.ok(book.getTitle().toUpperCase() + " has been added to the database.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the item: " + e.getMessage());
        }
    }

    @PostMapping("/api/books/add")
    public ResponseEntity<String> addItem(@RequestBody DTO_Post_Book DTOBook, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("[x] you have to be logged in"); return null; }

        user = serviceAccount.findOne(user.getId());
        if (user.getAccountRole() != Account_Role.AUTHOR) { System.err.println("[x] not author: " + user); return null; }

        try {
            // TODO: addgenres for the item

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

    @DeleteMapping("/api/admin/removeitem/{id}")
    public ResponseEntity<String> removeItem(@PathVariable("id") Long id, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        try {
            ShelfItem item = serviceShelfItem.findOne(id);
            if (item == null) { return ResponseEntity.badRequest().body("The item doesn't exist."); }

            serviceShelfItem.remove(item);
            serviceBook.remove(item.getBook());

            return ResponseEntity.ok("Successfully removed the item.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not remove the item: " + e.getMessage());
        }
    }

    @PutMapping("/api/admin/update/item/{id}")
    public ResponseEntity<String> updateItem(@PathVariable("id") Long itemId, @RequestBody DTO_Post_Book newInfo, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        ShelfItem item = serviceShelfItem.findOne(itemId);
        if (item == null) { return ResponseEntity.badRequest().body("This item doesn't exist."); }

        Book targetBook = item.getBook();

        if (!newInfo.getTitle().isEmpty()) { targetBook.setTitle(newInfo.getTitle()); }
        if (newInfo.getReleaseDate() != null) { targetBook.setReleaseDate(newInfo.getReleaseDate()); }
        if (newInfo.getNumOfPages() > 0) { targetBook.setNumOfPages(newInfo.getNumOfPages()); }
        if (!newInfo.getIsbn().isEmpty() && !newInfo.getIsbn().equals(targetBook.getIsbn())) {
            Book book = serviceBook.findByIsbn(newInfo.getIsbn());
            if (book != null) { return ResponseEntity.badRequest().body("A book with this ISBN already exists."); }
            targetBook.setIsbn(newInfo.getIsbn());
        }

        targetBook.setDescription(newInfo.getDescription());
        targetBook.setCoverPhoto(newInfo.getCoverPhoto());

        targetBook.setBookGenres(new ArrayList<BookGenre>());
        for (String genreName : newInfo.getGenreNames()) {
            BookGenre genre = serviceBookGenre.findOne(genreName);
            System.out.println(serviceBookGenre.findOne(genreName));
            if (genre == null || genre.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Genre " + genreName + " does not exist.");
            }
            targetBook.getBookGenres().add(genre);
        }

        serviceBook.save(targetBook);
        return ResponseEntity.ok("Successfully updated the item/book.");
    }

    @PutMapping("/api/admin/update/author/{id}")
    public ResponseEntity<String> updateUnactivated(@PathVariable("id") Long id, @RequestBody DTO_Post_AccountAuthorUpdate newInfo, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        AccountAuthor author = serviceAccountAuthor.findOne(id);
        if (author == null) { return ResponseEntity.badRequest().body("This author doesn't exist."); }

        if (author.isAccountActivated()) { return ResponseEntity.badRequest().body("Admins can not edit activated author accounts."); }

        if (newInfo.getFirstName() != null && !newInfo.getFirstName().isEmpty() && !newInfo.getFirstName().equals(author.getFirstName()))  author.setFirstName(newInfo.getFirstName());
        if (newInfo.getLastName() != null && !newInfo.getLastName().isEmpty() && !newInfo.getLastName().equals(author.getLastName()))  author.setLastName(newInfo.getLastName());
        if (newInfo.getUsername() != null && !newInfo.getUsername().isEmpty() && !newInfo.getUsername().equals(author.getUsername()))  author.setUsername(newInfo.getUsername());
        if (newInfo.getDateOfBirth() != null && newInfo.getDateOfBirth() != null && !newInfo.getDateOfBirth().equals(author.getDateOfBirth()))  author.setDateOfBirth(newInfo.getDateOfBirth());
        if (newInfo.getDescription() != null && !newInfo.getDescription().isEmpty() && !newInfo.getDescription().equals(author.getDescription()))  author.setDescription(newInfo.getDescription());
        if (newInfo.getProfilePicture() != null && !newInfo.getProfilePicture().isEmpty() && !newInfo.getProfilePicture().equals(author.getProfilePicture()))  author.setProfilePicture(newInfo.getProfilePicture());

        serviceAccountAuthor.save(author);
        return ResponseEntity.ok("Author info updated.");
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<DTO_View_Account>> getUsers(HttpSession session) {
        List<Account> userList = serviceAccount.findAll();

        List<DTO_View_Account> dtos = new ArrayList<>();
        for (Account u : userList) {
            DTO_View_Account dto = new DTO_View_Account(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/users/username={username}")
    public Account getUser(@PathVariable(name = "username") String username, HttpSession session) {
        return serviceAccount.findOneByUsername(username);
    }

    @GetMapping("/api/users/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        return serviceAccount.findOne(id);
    }

    @GetMapping("/api/authors")
    public ResponseEntity<List<DTO_View_AccountAuthor>> getAuthors(HttpSession session) {
        List<AccountAuthor> userList = serviceAccountAuthor.findAllAuthors();

        List<DTO_View_AccountAuthor> dtos = new ArrayList<>();
        for (AccountAuthor u : userList) {
            DTO_View_AccountAuthor dto = new DTO_View_AccountAuthor(new Account(u.getFirstName(), u.getLastName(), u.getUsername(), u.getMailAddress(), u.getPassword(), u.getDateOfBirth(), u.getMailAddress(), u.getDescription(), u.getAccountRole()), u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/genres")
    public ResponseEntity<List<DTO_View_BookGenre>> getGenres(HttpSession session) {
        List<BookGenre> genreList = serviceBookGenre.findAll();

        BookGenre genre = (BookGenre) session.getAttribute("genre");
        if (genre == null) { System.out.println("No session"); }
        else               { System.out.println(genre);        }

        List<DTO_View_BookGenre> dtos = new ArrayList<>();
        for (BookGenre g : genreList) { DTO_View_BookGenre dto = new DTO_View_BookGenre(g); dtos.add(dto); }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/genres/{id}")
    public BookGenre getGenre(@PathVariable(name = "id") Long id, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return serviceBookGenre.findOne(id);
    }

    @GetMapping("/api/genres/name={name}")
    public BookGenre getGenre(@PathVariable(name = "name") String name, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return serviceBookGenre.findOne(name);
    }

    @GetMapping("/api/reviews")
    public ResponseEntity<List<DTO_View_BookReviewNoShelves>> getBookReviews(HttpSession session) {
        List<BookReview> bookReviews = serviceBookReview.findAll();

        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        if (bookReview == null) { System.out.println("No session"); }
        else                    { System.out.println(bookReview);         }

        List<DTO_View_BookReviewNoShelves> dtos = new ArrayList<>();
        for (BookReview b : bookReviews) { DTO_View_BookReviewNoShelves dto = new DTO_View_BookReviewNoShelves(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/reviews/{id}")
    public BookReview getBookReview(@PathVariable(name = "id") Long id, HttpSession session) {
        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        System.out.println(bookReview);
        session.invalidate();
        return serviceBookReview.findOne(id);
    }

    @GetMapping("/api/users/{userId}/shelf/{shelfId}")
    public Shelf getUserShelf(@PathVariable(name = "userId") Long userID, @PathVariable(name = "shelfId") Long shelfID, HttpSession session) {
        Account user = serviceAccount.findOne(userID);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(shelfID)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/users/{userId}/shelf/name={shelfName}")
    public Shelf getUserShelf(@PathVariable(name = "userId") Long userID, @PathVariable(name = "shelfName") String shelfname, HttpSession session) {
        Account user = serviceAccount.findOne(userID);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getName().equalsIgnoreCase(shelfname)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/users/username={userName}/shelf/{shelfId}")
    public Shelf getUserShelf(@PathVariable(name = "userName") String username, @PathVariable(name = "shelfId") Long shelfID, HttpSession session) {
        Account user = serviceAccount.findOneByUsername(username);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(shelfID)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/users/username={userName}/shelf/name={shelfName}")
    public Shelf getUserShelf(@PathVariable(name = "userName") String username, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = serviceAccount.findOneByUsername(username);
        if (user == null) { return null; }

        for (Shelf s : user.getShelves()) {
            if (s.getName().equalsIgnoreCase(shelfName)) {
                return s;
            }
        }

        return null;
    }

    @GetMapping("/api/users/{userId}/shelves")
    public List<Shelf> getUserShelves(@PathVariable(name = "userId") Long userID, HttpSession session) {
        Account user = serviceAccount.findOne(userID);
        if (user == null) { return null; }

        return user.getShelves();
    }

    @GetMapping("/api/users/username={userName}/shelves")
    public List<Shelf> getUserShelves(@PathVariable(name = "userName") String username, HttpSession session) {
        Account user = serviceAccount.findOneByUsername(username);
        if (user == null) { return null; }

        return user.getShelves();
    }

    @GetMapping("/api/items")
    public ResponseEntity<List<DTO_View_ShelfItem>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = serviceShelfItem.findAll();

        List<DTO_View_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) {
            DTO_View_ShelfItem dto = new DTO_View_ShelfItem(b);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/items/{id}")
    public ShelfItem getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        return serviceShelfItem.findOne(id);
    }

    @GetMapping("/api/items/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        return serviceShelfItem.findByBook(serviceBook.findOne(title));
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<DTO_View_Book>> getBooks(HttpSession session) {
        List<Book> books = serviceBook.findAll();

        List<DTO_View_Book> dtos = new ArrayList<>();
        for (Book b : books) {
            DTO_View_Book dto = new DTO_View_Book(b);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/books/{id}")
    public Book getBook(@PathVariable(name = "id") Long id, HttpSession session) {
        return serviceBook.findOne(id);
    }

    @GetMapping("/api/books/title={title}")
    public Book getBook(@PathVariable(name = "title") String title, HttpSession session) {
        return serviceBook.findOne(title);
    }

    @GetMapping("/api/books/search={search}")
    public ResponseEntity<List<DTO_View_ShelfItem>> searchItems(@PathVariable(name = "search") String search, HttpSession session) {
        if (search.isEmpty()) { return null; }

        List<DTO_View_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem i : serviceShelfItem.findAll()) {
            // search by title / description
            if (i.getBook().getTitle().toLowerCase().contains(search.toLowerCase())) {
                DTO_View_ShelfItem dto = new DTO_View_ShelfItem(i); dtos.add(dto);
            }
        }

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/api/sendactivation/{id}")
    public AccountActivationRequest sendActivationRequest(@PathVariable(name = "id") Long id, @RequestBody DTO_Post_AccountActivationRequest dtoPostAccountActivationRequest, HttpSession session) {

        AccountAuthor aa = serviceAccountAuthor.findOne(id);
        if (aa == null) { System.err.println("[x] account existn't"); return null; }

        try {
            AccountActivationRequest aar = new AccountActivationRequest(
                    dtoPostAccountActivationRequest.getMailAddress(),
                    dtoPostAccountActivationRequest.getPhoneNumber(),
                    dtoPostAccountActivationRequest.getMessage(),
                    LocalDate.now(),
                    AccountActivationRequest_Status.WAITING,
                    aa
            );

            serviceAccountActivationRequest.save(aar);

            return aar;
        } catch (Exception e) {
            System.err.println("[x] failed to send activation req: " + e.getMessage());
            return null;
        }
    }



}
