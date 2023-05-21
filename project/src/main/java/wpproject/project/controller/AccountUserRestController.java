package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.AccountUserDTO;
import wpproject.project.dto.BookDTO;
import wpproject.project.dto.BookGenreDTO;
import wpproject.project.model.AccountUser;
import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;
import wpproject.project.service.AccountUserService;
import wpproject.project.service.BookGenreService;
import wpproject.project.service.BookService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountUserRestController {
    @Autowired
    private AccountUserService accountUserService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookGenreService genreService;

    @GetMapping("/api")
    public String welcome() {
        return "Hello from api";
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<AccountUserDTO>> getUsers(HttpSession session) {
        List<AccountUser> userList = accountUserService.findAll();

        AccountUser user = (AccountUser) session.getAttribute("user");
        if (user == null) {
            System.out.println("No session");
        } else {
            System.out.println(user);
        }

        List<AccountUserDTO> dtos = new ArrayList<>();
        for (AccountUser u : userList) {
            AccountUserDTO dto = new AccountUserDTO(u);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/user/name={username}")
    public AccountUser getUser(@PathVariable(name = "username") String username, HttpSession session) {
        AccountUser user = (AccountUser) session.getAttribute("user");
        System.out.println(user);
        session.invalidate();
        return accountUserService.findOne(username);
    }

    @GetMapping("/api/user/{id}")
    public AccountUser getUser(@PathVariable(name = "id") Long id, HttpSession session) {
        AccountUser user = (AccountUser) session.getAttribute("user");
        System.out.println(user);
        session.invalidate();
        return accountUserService.findOne(id);
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDTO>> getBooks(HttpSession session) {
        List<Book> bookList = bookService.findAll();

        Book book = (Book) session.getAttribute("book");
        if (book == null) {
            System.out.println("No session");
        } else {
            System.out.println(book);
        }

        List<BookDTO> dtos = new ArrayList<>();
        for (Book b : bookList) {
            BookDTO dto = new BookDTO(b);
            dtos.add(dto);
        }

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

    @GetMapping("/api/genres")
    public ResponseEntity<List<BookGenreDTO>> getGenres(HttpSession session) {
        List<BookGenre> genreList = genreService.findAll();

        BookGenre genre = (BookGenre) session.getAttribute("genre");
        if (genre == null) {
            System.out.println("No session");
        } else {
            System.out.println(genre);
        }

        List<BookGenreDTO> dtos = new ArrayList<>();
        for (BookGenre g : genreList) {
            BookGenreDTO dto = new BookGenreDTO(g);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/genre/name={name}")
    public BookGenre getGenre(@PathVariable(name = "name") String name, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return genreService.findOne(name);
    }

    @GetMapping("/api/genre/{id}")
    public BookGenre getGenre(@PathVariable(name = "id") Long id, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return genreService.findOne(id);
    }
}
