package wpproject.project.controller;

import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
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

    // TODO: replace ResponseEntity.ok ResponseEntity.badRequest


    // TODO: DONE REQ

    // MY ACCOUNT

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody DTO_Post_AccountLogin DTOAccountLogin, HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return new ResponseEntity<>("already logged in", HttpStatus.BAD_REQUEST); }

        if (DTOAccountLogin.getUsername().isEmpty() || DTOAccountLogin.getPassword().isEmpty())  { return new ResponseEntity<>("invlaid login data", HttpStatus.BAD_REQUEST); }

        Account account = serviceAccount.findOneByUsername(DTOAccountLogin.getUsername());
        if (account == null) { account = serviceAccount.findOneByMailAddress((DTOAccountLogin.getUsername())); }
        if (account == null) { return new ResponseEntity<>("account not found", HttpStatus.NOT_FOUND);  }
        if (!account.getPassword().equals(DTOAccountLogin.getPassword())) { return new ResponseEntity<>("wrong password", HttpStatus.UNAUTHORIZED); }

        if (account != null && account.getAccountRole() == Account_Role.AUTHOR) {
            if (!serviceAccountAuthor.findById(account.getId()).isAccountActivated()) { return new ResponseEntity<>("author account not activated", HttpStatus.UNAUTHORIZED);  }
        }

        session.setAttribute("user", account);
        return new ResponseEntity<>("logged in as: " + account.getUsername(), HttpStatus.OK);
    }

    @GetMapping("/api/myaccount")
    public ResponseEntity<DTO_View_AccountAsAnon> myaccount(HttpSession session){
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); }

        DTO_View_AccountAsAnon dto = new DTO_View_AccountAsAnon(serviceAccount.findOne(user.getId()));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> registerAccount(@RequestBody DTO_Post_AccountRegister accountRequest, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user != null) { return new ResponseEntity<>("already logged in", HttpStatus.BAD_REQUEST); }

        try {
            Account account = serviceAccount.findOneByMailAddress(accountRequest.getMailAddress());
            if (account != null) { return new ResponseEntity<>("mail exists: " + accountRequest.getMailAddress(), HttpStatus.CONFLICT); }
            account = serviceAccount.findOneByUsername(accountRequest.getUsername());
            if (account != null) { return new ResponseEntity<>("username exists: " + accountRequest.getUsername(), HttpStatus.CONFLICT); }

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
            return new ResponseEntity<>("registered", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return new ResponseEntity<>("already logged out", HttpStatus.BAD_REQUEST); }

        session.invalidate();
        return new ResponseEntity<>("logged out: " + user.getUsername(), HttpStatus.OK);
    }

    // USERS

    @GetMapping("/api/users")
    public ResponseEntity<List<DTO_View_AccountAsAnon>> getUsers(HttpSession session) {
        List<Account> userList = serviceAccount.findAll();

        List<DTO_View_AccountAsAnon> dtos = new ArrayList<>();
        for (Account u : userList) {
            DTO_View_AccountAsAnon dto = new DTO_View_AccountAsAnon(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/users/{id}")
    public Account getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        return serviceAccount.findOne(id);
    }

    // AUTHORS

    @GetMapping("/api/authors")
    public ResponseEntity<List<DTO_View_AccountAuthorAsAnon>> getAuthors(HttpSession session) {
        List<AccountAuthor> authors = serviceAccountAuthor.findAllAuthors();

        List<DTO_View_AccountAuthorAsAnon> dtos = new ArrayList<>();
        for (AccountAuthor a : authors) {
            DTO_View_AccountAuthorAsAnon dto = new DTO_View_AccountAuthorAsAnon(a);

            dtos.add(dto);
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // ITEMS

    @GetMapping("/api/items")
    public ResponseEntity<List<DTO_View_ShelfItem>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = serviceShelfItem.findAll();

        List<DTO_View_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) {
            DTO_View_ShelfItem dto = new DTO_View_ShelfItem(b);
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/api/items/{id}")
    public ResponseEntity<DTO_View_ShelfItem> getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        return new ResponseEntity<>(new DTO_View_ShelfItem(serviceShelfItem.findOne(id)), HttpStatus.OK);
    }

    @GetMapping("/api/items/search={search}")
    public ResponseEntity<List<DTO_View_ShelfItem>> searchItems(@PathVariable(name = "search") String search, HttpSession session) {
        if (search.isEmpty()) { return null; }

        List<DTO_View_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem i : serviceShelfItem.findAll()) {
            // search by title / description
            if (i.getBook().getTitle().toLowerCase().contains(search.toLowerCase()) || i.getBook().getDescription().toLowerCase().contains(search.toLowerCase())) {
                DTO_View_ShelfItem dto = new DTO_View_ShelfItem(i); dtos.add(dto);
            }
        }

        return ResponseEntity.ok(dtos);
    }

    // GENRES

    @GetMapping("/api/genres")
    public ResponseEntity<List<DTO_View_BookGenre>> getGenres(HttpSession session) {
        List<DTO_View_BookGenre> dtos = new ArrayList<>();
        for (BookGenre g : serviceBookGenre.findAll()) {
            dtos.add(new DTO_View_BookGenre(g));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/api/genres/{id}")
    public ResponseEntity<DTO_View_BookGenre> getGenre(@PathVariable(name = "id") Long id, HttpSession session) {
        return new ResponseEntity<>(new DTO_View_BookGenre(serviceBookGenre.findOne(id)), HttpStatus.OK);
    }



    // BOOKS

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



    // SHELVES

    @PostMapping("/api/myaccount/shelves/add")
    public ResponseEntity<String> addShelf(@RequestBody DTO_Post_Shelf dtoPostShelf, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return new ResponseEntity<>("not logged in", HttpStatus.UNAUTHORIZED); }
        user = serviceAccount.findOne(user.getId());

        try {
            for (Shelf shelf : user.getShelves()) {
                if (shelf.getName().equals(dtoPostShelf.getName())) {
                    return new ResponseEntity<>("shelf exists: " + dtoPostShelf.getName(), HttpStatus.CONFLICT);
                }
            }

            Shelf newShelf = new Shelf(dtoPostShelf.getName(), false);
            serviceShelf.save(newShelf);

            user.getShelves().add(newShelf);
            serviceAccount.save(user);

            return new ResponseEntity<>("added shelf: " + dtoPostShelf.getName(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("failed to add shelf: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/myaccount/shelves/remove/{id}")
    public ResponseEntity<String> removeShelfID(@PathVariable(name = "id") Long id, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return new ResponseEntity<>("not logged in", HttpStatus.UNAUTHORIZED); }
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

    private ResponseEntity<String> removeShelf(Account user, Shelf shelf) {
        if (shelf == null) { return ResponseEntity.badRequest().body("shelf does not exist"); }
        if (shelf.isPrimary()) { return ResponseEntity.badRequest().body("primary shelves can not be removed"); }

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
            return new ResponseEntity<>("Could not remove the shelf" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // SHELVES -> ADD / REMOVE BOOK

    @PostMapping("/api/myaccount/shelves/{shelfId}/addbook/{bookId}")
    public ResponseEntity<String> addToShelfIdId(@PathVariable(name = "bookId") Long bookID, @PathVariable(name = "shelfId") Long shelfID, @RequestBody(required = false) DTO_Post_BookReview review, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = serviceAccount.findOne(user.getId());

        ShelfItem targetItem = serviceShelfItem.findByBook(serviceBook.findOne(bookID));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        Shelf targetShelf = null;
        for (Shelf s : user.getShelves()) {
            if (s.getId().equals(shelfID)) {
                targetShelf = s;
                break;
            }
        }

        if (targetShelf == null) {
            return ResponseEntity.badRequest().body("Shelf with id " + shelfID + " does not exist.");
        }

        return addToShelf(user, targetItem, targetShelf, review);
    }

    private ResponseEntity<String> addToShelf(Account user, ShelfItem targetItem, Shelf targetShelf, DTO_Post_BookReview review) {
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

    @PostMapping("/api/myaccount/shelves/{shelfId}/removebook/{bookId}")
    public ResponseEntity<String> userRemoveBook(@PathVariable(name = "shelfId") Long shelfId, @PathVariable(name = "bookId") Long bookId, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }
        user = serviceAccount.findOne(user.getId());

        boolean doesExist = false;
        boolean inPrimaryShelf = false;
        Shelf targetShelf = new Shelf();
        for (Shelf s : user.getShelves()) {
            if (!s.getId().equals(shelfId)) { continue; }

            doesExist = true;
            if (s.isPrimary()) { inPrimaryShelf = true; }
            targetShelf = s;
            break;
        }
        if (!doesExist) { return ResponseEntity.badRequest().body("Could not find that shelf  in this user's shelf list."); }

        return userRemoveBook(serviceAccount.findOne(user.getId()), serviceBook.findOne(bookId), targetShelf, inPrimaryShelf);
    }

    private ResponseEntity<String> userRemoveBook(Account user, Book book, Shelf targetShelf, boolean inPrimaryShelf) {
        String msg = "";
        ShelfItem targetItem = serviceShelfItem.findByBook(book);

        if (!(targetShelf.getName().equalsIgnoreCase("read") || targetShelf.getName().equalsIgnoreCase("currentlyreading") || targetShelf.getName().equalsIgnoreCase("wanttoread"))) {
            for (Shelf s : user.getShelves().subList(3, user.getShelves().size())) {
                if (!s.getId().equals(targetShelf.getId())) { continue; }

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
            if (!s.getId().equals(targetShelf.getId())) { continue; }

            Iterator<ShelfItem> iterator = s.getShelfItems().iterator();
            while (iterator.hasNext()) {
                ShelfItem i = iterator.next();
                if (i.getId().equals(targetItem.getId())) {
                    msg += i.getBook().getTitle().toUpperCase() + " (" + i.getBook().getId() + ") has been removed from " + s.getName().toUpperCase() + ".\n";

                    // Remove the associated review
                    if (targetShelf.getName().equalsIgnoreCase("Read")) {
                        msg += removeReview(user, i, book);
                    }

                    iterator.remove();
                    serviceShelf.save(user.getShelves());

                    return ResponseEntity.ok(msg);
                }
            }
        }

        return ResponseEntity.badRequest().body("Could not find " + book.getTitle().toUpperCase() + " (" + book.getId() + ") in " + targetShelf.getName().toUpperCase() + ".");
    }


    // ADMIN ACTIVATIONS


    @PostMapping("/api/sendactivation/{id}")
    public ResponseEntity<String> sendActivationRequest(@PathVariable(name = "id") Long id, @RequestBody DTO_Post_AccountActivationRequest dtoPostAccountActivationRequest, HttpSession session) {

        AccountAuthor aa = serviceAccountAuthor.findOne(id);
        if (aa == null) { return new ResponseEntity<>("can't find author with id: " + id, HttpStatus.NOT_FOUND); }

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

            return new ResponseEntity<>("activation request sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("failed to send activation req: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PostMapping("/api/admin/activations/{id}/accept")
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

    @PostMapping("/api/admin/activations/{id}/reject")
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

    // MY ACCOUNT -> UPDATE

    @PutMapping("/api/myaccount/update")
    public ResponseEntity<String> updateUser(@RequestBody DTO_Post_AccountUpdate newInfo, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return new ResponseEntity<>("not logged in", HttpStatus.UNAUTHORIZED); }

        user = serviceAccount.findOne(user.getId());

        boolean failed = false;

        if (newInfo.getFirstName() != null      && !newInfo.getFirstName().isEmpty() && !newInfo.getFirstName().equals(user.getFirstName()))                user.setFirstName(newInfo.getFirstName());
        if (newInfo.getLastName() != null       && !newInfo.getLastName().isEmpty() && !newInfo.getLastName().equals(user.getLastName()))                   user.setLastName(newInfo.getLastName());
        if (newInfo.getUsername() != null       && !newInfo.getUsername().isEmpty() && !newInfo.getUsername().equals(user.getUsername()))                   user.setUsername(newInfo.getUsername());
        if (newInfo.getDateOfBirth() != null    && !newInfo.getDateOfBirth().equals(user.getDateOfBirth()))                                                 user.setDateOfBirth(newInfo.getDateOfBirth());
        if (newInfo.getDescription() != null    && !newInfo.getDescription().isEmpty() && !newInfo.getDescription().equals(user.getDescription()))          user.setDescription(newInfo.getDescription());
        if (newInfo.getProfilePicture() != null && !newInfo.getProfilePicture().isEmpty() && !newInfo.getProfilePicture().equals(user.getProfilePicture())) user.setProfilePicture(newInfo.getProfilePicture());

        // update mail
        if (newInfo.getMailAddress() != null && !newInfo.getMailAddress().isEmpty()) {

            if (newInfo.getPassword().equals(user.getPassword())) { // current password must match
                user.setMailAddress(newInfo.getMailAddress());
            }
            else {
                failed = true;
            }
        }

        // update password
        if (newInfo.getNewPassword() != null && !newInfo.getNewPassword().isEmpty()) {

            if (newInfo.getPassword().equals(user.getPassword())) { // current password must match
                user.setPassword(newInfo.getNewPassword());
            }
            else {
                failed = true;
            }
        }


        if (failed) {
            return new ResponseEntity<>("must enter current password to update mail/password", HttpStatus.FORBIDDEN);
        }
        else {
            serviceAccount.save(user);
            return new ResponseEntity<>("account updated", HttpStatus.OK);
        }
    }

    //
    // TODO: ALL
    //


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
    public ResponseEntity<?> admin_addAuthor(@RequestBody DTO_Post_AccountAuthor DTOAccountAuthorNew, HttpSession session){
        if (!isAdmin(session)) { return null; }

        try {
            // check if exists by mail/username
            if (serviceAccount.findOneByMailAddress(DTOAccountAuthorNew.getMailAddress()) != null) {
                return new ResponseEntity<>("[x] can't add new user (author), mail exists:" + DTOAccountAuthorNew.getMailAddress(), HttpStatus.BAD_REQUEST);
            }
            if (serviceAccount.findOneByUsername(DTOAccountAuthorNew.getUsername()) != null) {
                return new ResponseEntity<>("[x] can't add new user (author), username exists:" + DTOAccountAuthorNew.getUsername(), HttpStatus.BAD_REQUEST);
            }

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
            return new ResponseEntity<>(newAuthor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("[x] failed to add new user (author): " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

            Book book = new Book(DTOBook.getTitle(), DTOBook.getCoverPhoto(), LocalDate.now(), DTOBook.getDescription(), DTOBook.getNumOfPages(), 0, DTOBook.getIsbn(), DTOBook.getGenres());
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
        for (BookGenre g : newInfo.getGenres()) {
            BookGenre genre = serviceBookGenre.findOne(g.getName());
            System.out.println(serviceBookGenre.findOne(g.getName()));
            if (genre == null || genre.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Genre " + g.getName() + " does not exist.");
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














    // USELESS FOR NOW

    @GetMapping("/api/users/username={username}")
    public Account getUser(@PathVariable(name = "username") String username, HttpSession session) {
        return serviceAccount.findOneByUsername(username);
    }

    @GetMapping("/api/genres/name={name}")
    public ResponseEntity<DTO_View_BookGenre> getGenre(@PathVariable(name = "name") String name, HttpSession session) {
        return new ResponseEntity<>(new DTO_View_BookGenre(serviceBookGenre.findOne(name)), HttpStatus.OK);
    }

    @GetMapping("/api/books/title={title}")
    public Book getBook(@PathVariable(name = "title") String title, HttpSession session) {
        return serviceBook.findOne(title);
    }


    // USELESS


    @PostMapping("/api/myaccount/shelves/remove/name={name}")
    public ResponseEntity<String> removeShelfName(@PathVariable(name = "name") String name, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return new ResponseEntity<>("not logged in", HttpStatus.UNAUTHORIZED); }
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

    @PostMapping("/api/myaccount/shelves/name={shelfName}/addbook/{bookId}")
    public ResponseEntity<String> addToShelfNameId(@PathVariable(name = "bookId") Long bookId, @PathVariable(name = "shelfName") String shelfName, @RequestBody(required = false) DTO_Post_BookReview review, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = serviceAccount.findOne(user.getId());

        ShelfItem targetItem = serviceShelfItem.findByBook(serviceBook.findOne(bookId));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

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

        return addToShelf(user, targetItem, targetShelf, review);
    }

    @PostMapping("/api/myaccount/shelves/name={shelfName}/addbook/isbn={isbn}")
    public ResponseEntity<String> addToShelfNameIsbn(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, @RequestBody(required = false) DTO_Post_BookReview review, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = serviceAccount.findOne(user.getId());

        ShelfItem targetItem = serviceShelfItem.findByBook(serviceBook.findByIsbn(isbn));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

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

        return addToShelf(user, targetItem, targetShelf, review);
    }

//    @PostMapping("/api/myaccount/shelves/name={shelfName}/removebook/{bookId}")
//    public ResponseEntity<String> userRemoveBookID(@PathVariable(name = "bookId") Long bookID, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
//        Account user = (Account) session.getAttribute("user");
//        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }
//
//        return userRemoveBook(serviceAccount.findOne(user.getId()), serviceBook.findOne(bookID), shelfName);
//    }
//
//    @PostMapping("/api/myaccount/shelves/name={shelfName}/removebook/isbn={isbn}")
//    public ResponseEntity<String> userRemoveBookISBN(@PathVariable(name = "isbn") String isbn, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
//        Account user = (Account) session.getAttribute("user");
//        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf."); }
//
//        return userRemoveBook(serviceAccount.findOne(user.getId()), serviceBook.findByIsbn(isbn), shelfName);
//    }

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

    @GetMapping("/api/items/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        return serviceShelfItem.findByBook(serviceBook.findOne(title));
    }

    @GetMapping("/api/books/search={search}")
    public ResponseEntity<List<DTO_View_Book>> searchBooks(@PathVariable(name = "search") String search, HttpSession session) {
        if (search.isEmpty()) { return null; }

        List<DTO_View_Book> dtos = new ArrayList<>();
        for (Book i : serviceBook.findAll()) {
            // search by title / description
            if (i.getTitle().toLowerCase().contains(search.toLowerCase()) || i.getDescription().toLowerCase().contains(search.toLowerCase())) {
                DTO_View_Book dto = new DTO_View_Book(i); dtos.add(dto);
            }
        }

        return ResponseEntity.ok(dtos);
    }




}
