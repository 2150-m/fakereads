package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.DTO_Book;
import wpproject.project.model.Book;
import wpproject.project.model.ShelfItem;
import wpproject.project.service.Service_Book;
import wpproject.project.service.Service_ShelfItem;

import java.time.LocalDate;

@RestController
public class Controller_Rest_AccountAuthor {

    @Autowired
    private Service_Book serviceBook;

    @Autowired
    private Service_ShelfItem serviceShelfItem;

    @PostMapping("/api/database/book/add")
    public ResponseEntity<String> addItem(@RequestBody DTO_Book DTOBook, HttpSession session) {
        try {
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
}
