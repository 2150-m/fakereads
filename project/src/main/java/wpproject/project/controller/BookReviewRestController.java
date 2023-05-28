package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.BookReviewDTO;
import wpproject.project.dto.BookReviewDTO_New;
import wpproject.project.model.*;
import wpproject.project.service.BookReviewService;
import wpproject.project.service.BookService;
import wpproject.project.service.ShelfItemService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BookReviewRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookReviewService bookReviewService;

    @Autowired
    private ShelfItemService shelfItemService;

    @GetMapping("/api/database/reviews")
    public ResponseEntity<List<BookReviewDTO>> getBookReviews(HttpSession session) {
        List<BookReview> bookReviews = bookReviewService.findAll();

        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        if (bookReview == null) { System.out.println("No session"); }
        else                    { System.out.println(bookReview);         }

        List<BookReviewDTO> dtos = new ArrayList<>();
        for (BookReview b : bookReviews) { BookReviewDTO dto = new BookReviewDTO(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/review/{id}")
    public BookReview getBookReview(@PathVariable(name = "id") Long id, HttpSession session) {
        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        System.out.println(bookReview);
        session.invalidate();
        return bookReviewService.findOne(id);
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
