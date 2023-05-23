package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.AccountRegisterDTO;
import wpproject.project.dto.BookReviewDTO;
import wpproject.project.dto.BookReviewNewDTO;
import wpproject.project.model.Account;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.model.Shelf;
import wpproject.project.service.BookReviewService;
import wpproject.project.service.BookService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BookReviewRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookReviewService bookReviewService;

    @GetMapping("/api/reviews")
    public ResponseEntity<List<BookReviewDTO>> getBookReviews(HttpSession session) {
        List<BookReview> bookReviews = bookReviewService.findAll();

        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        if (bookReview == null) { System.out.println("No session"); }
        else                    { System.out.println(bookReview);         }

        List<BookReviewDTO> dtos = new ArrayList<>();
        for (BookReview b : bookReviews) { BookReviewDTO dto = new BookReviewDTO(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/review/{id}")
    public BookReview getBookReview(@PathVariable(name = "id") Long id, HttpSession session) {
        BookReview bookReview = (BookReview) session.getAttribute("bookReview");
        System.out.println(bookReview);
        session.invalidate();
        return bookReviewService.findOne(id);
    }

    @PostMapping("/api/book/{bookId}/addreview")
    public ResponseEntity addReview(@PathVariable(name = "bookId") Long bookId, @RequestBody BookReviewNewDTO bookReviewNewDTO, HttpSession session) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) { return ResponseEntity.badRequest().body("not logged in / can't post review"); }

        Book book = bookService.findOne(bookId);
        if (book == null) { return ResponseEntity.badRequest().body("book does not exist"); }

        /// TODO: fix only one review per user
        BookReview bookReview = bookReviewService.findByAccount(user);
        if (bookReview != null) { return ResponseEntity.badRequest().body("user already has review"); }


        BookReview newBookReview = new BookReview(bookReviewNewDTO.getRating(), bookReviewNewDTO.getText(), LocalDate.now(), user);
        bookReviewService.save(newBookReview);

        return ResponseEntity.ok("posted review");
    }

}
