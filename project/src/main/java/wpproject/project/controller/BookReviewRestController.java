package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.AccountRegisterDTO;
import wpproject.project.dto.BookReviewDTO;
import wpproject.project.model.Account;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.model.Shelf;
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

//    @PostMapping("/api/addreview")
//    public ResponseEntity addReview(@RequestBody BookReviewDTO bookReviewDTO, HttpSession session) {
//
//        try {
//            BookReview bookReview = bookReviewService.findOne(bookReviewDTO.getAccount().getId());
//            if (account != null) { return ResponseEntity.badRequest().body("User already reviewed the book."); }
//
//            account = new Account(accountRequest.getFirstName(), accountRequest.getLastName(), accountRequest.getUsername(), accountRequest.getMailAddress(), accountRequest.getPassword());
//
//            Shelf shelf_WantToRead = new Shelf("WantToRead", true);
//            Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
//            Shelf shelf_Read = new Shelf("Read", true);
//            shelfService.save(shelf_WantToRead);
//            shelfService.save(shelf_CurrentlyReading);
//            shelfService.save(shelf_Read);
//            account.setShelves(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));
//
//            accountService.save(account);
//
//            session.setAttribute("user", account);
//            return ResponseEntity.ok("Succesfully registered: " + account.getUsername());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register: " + e.getMessage());
//        }
//    }

}
