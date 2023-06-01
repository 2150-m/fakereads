package wpproject.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.service.BookService;

@RestController
public class Controller_Rest_Book {
    @Autowired
    private BookService bookService;

//    @GetMapping("/api/books")
//    public ResponseEntity<List<BookDTO>> getBooks(HttpSession session) {
//        List<Book> bookList = bookService.findAll();
//
//        Book book = (Book) session.getAttribute("book");
//        if (book == null) { System.out.println("No session"); }
//        else              { System.out.println(book);         }
//
//        List<BookDTO> dtos = new ArrayList<>();
//        for (Book b : bookList) { BookDTO dto = new BookDTO(b); dtos.add(dto); }
//
//        return ResponseEntity.ok(dtos);
//    }
//
//    @GetMapping("/api/book/{id}")
//    public Book getBook(@PathVariable(name = "id") Long id, HttpSession session) {
//        Book book = (Book) session.getAttribute("book");
//        System.out.println(book);
//        session.invalidate();
//        return bookService.findOne(id);
//    }
//
//    @GetMapping("/api/book/title={title}")
//    public Book getBook(@PathVariable(name = "title") String title, HttpSession session) {
//        Book book = (Book) session.getAttribute("book");
//        System.out.println(book);
//        session.invalidate();
//        return bookService.findOne(title);
//    }
//
//    @GetMapping("/api/book/search={search}")
//    public ResponseEntity<List<BookDTO>> searchBooks(@PathVariable(name = "search") String search, HttpSession session) {
//
//        List<Book> bookList = bookService.findAll();
//
//        List<BookDTO> dtos = new ArrayList<>();
//        for (Book b : bookList) {
//
//            // search by title / description
//            if (b.getTitle().contains(search) || b.getDescription().contains(search)) {
//                BookDTO dto = new BookDTO(b); dtos.add(dto);
//            }
//        }
//
//        return ResponseEntity.ok(dtos);
//    }
}
