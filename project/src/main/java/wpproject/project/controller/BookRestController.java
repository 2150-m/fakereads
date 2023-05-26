package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.BookDTO;
import wpproject.project.model.Book;
import wpproject.project.service.BookService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookRestController {
    @Autowired
    private BookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDTO>> getBooks(HttpSession session) {
        List<Book> bookList = bookService.findAll();

        Book book = (Book) session.getAttribute("book");
        if (book == null) { System.out.println("No session"); }
        else              { System.out.println(book);         }

        List<BookDTO> dtos = new ArrayList<>();
        for (Book b : bookList) { BookDTO dto = new BookDTO(b); dtos.add(dto); }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/book/{id}")
    public Book getBook(@PathVariable(name = "id") Long id, HttpSession session) {
        Book book = (Book) session.getAttribute("book");
        System.out.println(book);
        session.invalidate();
        return bookService.findOne(id);
    }

    @GetMapping("/api/book/name={title}")
    public Book getBook(@PathVariable(name = "title") String title, HttpSession session) {
        Book book = (Book) session.getAttribute("book");
        System.out.println(book);
        session.invalidate();
        return bookService.findOne(title);
    }

}
