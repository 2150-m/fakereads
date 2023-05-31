package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.BookReviewDTO_New;
import wpproject.project.dto.BookReviewDTO_NoShelves;
import wpproject.project.model.*;
import wpproject.project.service.AccountService;
import wpproject.project.service.BookReviewService;
import wpproject.project.service.BookService;
import wpproject.project.service.ShelfItemService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class BookReviewRestController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private BookReviewService bookReviewService;

    @GetMapping("/api/database/reviews")
    public ResponseEntity<List<BookReviewDTO_NoShelves>> getBookReviews(HttpSession session) {
        List<BookReview> bookReviews = bookReviewService.findAll();

        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        if (bookReview == null) { System.out.println("No session"); }
        else                    { System.out.println(bookReview);         }

        List<BookReviewDTO_NoShelves> dtos = new ArrayList<>();
        for (BookReview b : bookReviews) { BookReviewDTO_NoShelves dto = new BookReviewDTO_NoShelves(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/review/{id}")
    public BookReview getBookReview(@PathVariable(name = "id") Long id, HttpSession session) {
        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        System.out.println(bookReview);
        session.invalidate();
        return bookReviewService.findOne(id);
    }

    @PutMapping("/api/user/update/review/book_id={id}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "id") Long bookId, @RequestBody BookReviewDTO_New newReviewDTO, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = accountService.findOne(user.getId());

        for (ShelfItem i : user.getShelves().get(2).getShelfItems()) {
            if (!i.getBook().getId().equals(bookId)) { continue; }

            // Update an existing review made by this user
            for (BookReview r : i.getBookReviews()) {
                if (r.getAccount() != null && !r.getAccount().getId().equals(user.getId())) { continue; }

                r.setRating(newReviewDTO.getRating());
                r.setText(newReviewDTO.getText());

                bookReviewService.save(r);
                return ResponseEntity.ok("Review updated.");
            }

            // A review made by this user doesn't exist
            return addNewReview(user, i, newReviewDTO);
        }

        return ResponseEntity.badRequest().body("Review update failed.");
    }

    // TODO: book review author in returned jsons missing

    @PutMapping("/api/user/update/review/book_isbn={isbn}")
    public ResponseEntity<String> updateReview(@PathVariable(name = "isbn") String isbn, @RequestBody BookReviewDTO_New newReviewDTO, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in."); }
        user = accountService.findOne(user.getId());

        for (ShelfItem i : user.getShelves().get(2).getShelfItems()) {
            if (!i.getBook().getIsbn().equals(isbn)) { continue; }

            // Update an existing review made by this user
            for (BookReview r : i.getBookReviews()) {
                if (r.getAccount() != null && !r.getAccount().getId().equals(user.getId())) { continue; }

                r.setRating(newReviewDTO.getRating());
                r.setText(newReviewDTO.getText());

                bookReviewService.save(r);
                return ResponseEntity.ok("Review updated.");
            }

            // A review made by this user doesn't exist
            return addNewReview(user, i, newReviewDTO);
        }

        return ResponseEntity.badRequest().body("Could not find a book with the ISBN " + isbn + " in this user's account.");
    }

    private ResponseEntity<String> addNewReview(Account user, ShelfItem i, BookReviewDTO_New newReviewDTO) {
        BookReview newReview = new BookReview(newReviewDTO.getRating(), newReviewDTO.getText(), LocalDate.now(), user);
        i.getBookReviews().add(newReview);
        bookReviewService.save(newReview);
        return ResponseEntity.ok("Review added.");
    }

//    @PostMapping("/api/user/add/review/{bookId}")
//    public ResponseEntity addReview(@PathVariable(name = "bookId") Long bookId, @RequestBody BookReviewDTO_New bookReviewDTONew, HttpSession session) {
//        Account user = (Account) session.getAttribute("user");
//        if (user == null) { return ResponseEntity.badRequest().body("You have to be logged in in order to post a review."); }
//
//        Book book = bookService.findOne(bookId);
//        if (book == null) { return ResponseEntity.badRequest().body("A book with this ID does not exist."); }
//
//        for (ShelfItem item : user.getShelves().get(2).getShelfItems()) {
//            if (item.getBook() == book) {
//                return ResponseEntity.badRequest().body("This user has already reviewed this book.");
//            }
//        }
//
////        BookReview bookReview = bookReviewService.findByAccount(user);
////        if (bookReview != null) { return ResponseEntity.badRequest().body("user already has review"); }
//
//        BookReview newBookReview = new BookReview(bookReviewDTONew.getRating(), bookReviewDTONew.getText(), LocalDate.now(), user);
//        bookReviewService.save(newBookReview);
//
//        return ResponseEntity.ok("posted review");
//    }
}
