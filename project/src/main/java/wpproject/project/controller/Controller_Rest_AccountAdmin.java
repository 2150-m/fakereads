package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.*;
import wpproject.project.model.*;
import wpproject.project.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller_Rest_AccountAdmin {

    @Autowired
    private Service_Account serviceAccount;
    @Autowired
    private Service_AccountAuthor serviceAccountAuthor;
    @Autowired
    private Service_AccountActivationRequest serviceAccountActivationRequest;
    @Autowired
    private Service_BookGenre serviceBookGenre;
    @Autowired
    private Service_Book serviceBook;
    @Autowired
    private Service_ShelfItem serviceShelfItem;
    @Autowired
    private Service_Shelf serviceShelf;

    private Boolean isAdmin(HttpSession session) {
        // must be logged in
        Account user = (Account) session.getAttribute("user");
        if (user == null) { System.err.println("[x] you have to be logged in"); return false; }

        // must be admin
        user = serviceAccount.findOne(user.getId());
        if (user.getAccountRole() != Account_Role.ADMINISTRATOR) { System.err.println("[x] not admin: " + user); return false; }

        return true;
    }

    @PostMapping("/api/user/admin/addauthor")
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

    @GetMapping("/api/user/admin/activations")
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

    @PostMapping("/api/user/admin/activation/{id}/accept")
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

    @PostMapping("/api/user/admin/activation/{id}/reject")
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

    @PostMapping("/api/user/admin/addgenre")
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

    @PostMapping("/api/user/admin/additem")
    public ResponseEntity<String> addItem(@RequestBody DTO_Post_Book DTOBook, HttpSession session) {
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

    @DeleteMapping("/api/user/admin/removeitem/{id}")
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

    @PutMapping("/api/user/admin/update/item/{id}")
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

    @PutMapping("/api/user/admin/update/author/{id}")
    public ResponseEntity<String> updateUnactivated(@PathVariable("id") Long id, @RequestBody DTO_Post_AccountAuthorUpdate newInfo, HttpSession session) {
        if (!isAdmin(session)) { return ResponseEntity.badRequest().body("Not admin;"); }

        AccountAuthor author = serviceAccountAuthor.findOne(id);
        if (author == null) { return ResponseEntity.badRequest().body("This author doesn't exist."); }

        if (author.isAccountActivated()) { return ResponseEntity.badRequest().body("Admins can not edit activated author accounts."); }

        if (!newInfo.getFirstName().isEmpty() && !newInfo.getFirstName().equals(author.getFirstName()))
            author.setFirstName(newInfo.getFirstName());
        if (!newInfo.getLastName().isEmpty() && !newInfo.getLastName().equals(author.getLastName()))
            author.setLastName(newInfo.getLastName());
        if (!newInfo.getUsername().isEmpty() && !newInfo.getUsername().equals(author.getUsername()))
            author.setUsername(newInfo.getUsername());
        if (newInfo.getDateOfBirth() != null && !newInfo.getDateOfBirth().equals(author.getDateOfBirth()))
            author.setDateOfBirth(newInfo.getDateOfBirth());
        if (!newInfo.getDescription().isEmpty() && !newInfo.getDescription().equals(author.getDescription()))
            author.setDescription(newInfo.getDescription());
        if (!newInfo.getProfilePicture().isEmpty() && !newInfo.getProfilePicture().equals(author.getProfilePicture()))
            author.setProfilePicture(newInfo.getProfilePicture());

//        serviceAccount.save(author);
        serviceAccountAuthor.save(author);
        return ResponseEntity.ok("Author info updated.");
    }
}
