package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.*;
import wpproject.project.model.*;
import wpproject.project.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller_Rest_Anon {

    @Autowired
    private Service_Account serviceAccount;
    @Autowired
    private Service_AccountAuthor serviceAccountAuthor;
    @Autowired
    private Service_BookGenre serviceBookGenre;
    @Autowired
    private Service_BookReview serviceBookReview;
    @Autowired
    private Service_ShelfItem serviceShelfItem;
    @Autowired
    private Service_Book serviceBook;
    @Autowired
    private Service_AccountActivationRequest serviceAccountActivationRequest;

    @GetMapping("/api/database/users")
    public ResponseEntity<List<DTO_View_Account>> getUsers(HttpSession session) {
        List<Account> userList = serviceAccount.findAll();

        List<DTO_View_Account> dtos = new ArrayList<>();
        for (Account u : userList) {
            DTO_View_Account dto = new DTO_View_Account(u);
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

    @GetMapping("/api/database/genres")
    public ResponseEntity<List<DTO_View_BookGenre>> getGenres(HttpSession session) {
        List<BookGenre> genreList = serviceBookGenre.findAll();

        BookGenre genre = (BookGenre) session.getAttribute("genre");
        if (genre == null) { System.out.println("No session"); }
        else               { System.out.println(genre);        }

        List<DTO_View_BookGenre> dtos = new ArrayList<>();
        for (BookGenre g : genreList) { DTO_View_BookGenre dto = new DTO_View_BookGenre(g); dtos.add(dto); }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/genre/{id}")
    public BookGenre getGenre(@PathVariable(name = "id") Long id, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return serviceBookGenre.findOne(id);
    }

    @GetMapping("/api/database/genre/name={name}")
    public BookGenre getGenre(@PathVariable(name = "name") String name, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return serviceBookGenre.findOne(name);
    }

    @GetMapping("/api/database/reviews")
    public ResponseEntity<List<DTO_View_BookReviewNoShelves>> getBookReviews(HttpSession session) {
        List<BookReview> bookReviews = serviceBookReview.findAll();

        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        if (bookReview == null) { System.out.println("No session"); }
        else                    { System.out.println(bookReview);         }

        List<DTO_View_BookReviewNoShelves> dtos = new ArrayList<>();
        for (BookReview b : bookReviews) { DTO_View_BookReviewNoShelves dto = new DTO_View_BookReviewNoShelves(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/review/{id}")
    public BookReview getBookReview(@PathVariable(name = "id") Long id, HttpSession session) {
        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        System.out.println(bookReview);
        session.invalidate();
        return serviceBookReview.findOne(id);
    }

    @GetMapping("/api/database/user/{userId}/shelf/{shelfId}")
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

    @GetMapping("/api/database/user/{userId}/shelf/name={shelfName}")
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

    @GetMapping("/api/database/user/username={userName}/shelf/{shelfId}")
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

    @GetMapping("/api/database/user/username={userName}/shelf/name={shelfName}")
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

    @GetMapping("/api/database/user/{userId}/shelves")
    public List<Shelf> getUserShelves(@PathVariable(name = "userId") Long userID, HttpSession session) {
        Account user = serviceAccount.findOne(userID);
        if (user == null) { return null; }

        return user.getShelves();
    }

    @GetMapping("/api/database/user/username={userName}/shelves")
    public List<Shelf> getUserShelves(@PathVariable(name = "userName") String username, HttpSession session) {
        Account user = serviceAccount.findOneByUsername(username);
        if (user == null) { return null; }

        return user.getShelves();
    }

    @GetMapping("/api/database/items")
    public ResponseEntity<List<DTO_View_ShelfItem>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = serviceShelfItem.findAll();

        List<DTO_View_ShelfItem> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) {
            DTO_View_ShelfItem dto = new DTO_View_ShelfItem(b);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/item/{id}")
    public ShelfItem getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return serviceShelfItem.findOne(id);
    }

    @GetMapping("/api/database/item/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return serviceShelfItem.findByBook(serviceBook.findOne(title));
    }

    @GetMapping("/api/database/item/search={search}")
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

    @PostMapping("/api/database/activation/{id}")
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
