package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.BookReviewDTO;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.service.BookReviewService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookReviewRestController {

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
}
