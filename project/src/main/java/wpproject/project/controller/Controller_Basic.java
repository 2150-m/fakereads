package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wpproject.project.dto.DTO_View_AccountAsAnon;
import wpproject.project.model.Account;
import wpproject.project.model.Account_Role;
import wpproject.project.model.Shelf;
import wpproject.project.model.ShelfItem;
import wpproject.project.service.Service_Account;
import wpproject.project.service.Service_Book;
import wpproject.project.service.Service_ShelfItem;

@Controller
public class Controller_Basic {
    @Autowired
    private Service_Account serviceAccount;

    @Autowired
    private Service_Book serviceBook;

    @Autowired
    private Service_ShelfItem serviceShelfItem;

    @GetMapping("/home")
    public String home() { return "index.html"; }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }


    @GetMapping("/items")
    public String items() { return "items.html"; }

    @GetMapping("/items/{id}")
    public String getBook(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("item_id", id);
        return "item.html";
    }

    @GetMapping("/finditembybook/{bookId}")
    public String finditembybook_id(@PathVariable(name = "bookId") Long bookId, Model model) {

        ShelfItem item = serviceShelfItem.findByBook(serviceBook.findOne(bookId));
        model.addAttribute("item_id", item.getId());
        return "item.html";
    }

    @GetMapping("/login")
    public String login() { return "login.html"; }

    @GetMapping("/myaccount")
    public String myaccount(HttpSession session, Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) { return "error.html"; }

        model.addAttribute("user_id", user.getId());
        return "user.html";
    }

    @GetMapping("/register")
    public String register() { return "register.html"; }

    @GetMapping("/logout")
    public String logout() { return "logout.html"; }

    @GetMapping("/users")
    public String users() { return "users.html"; }

    @GetMapping("/authors")
    public String authors() { return "authors.html"; }

    @GetMapping("/users/{id}")
    public String users_id(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("user_id", id);
        return "user.html";
    }

    @GetMapping("/genres")
    public String genres() { return "genres.html"; }

    @GetMapping("/genres/{id}")
    public String genres_id(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("genre_id", id);
        return "genre.html";
    }

//    @GetMapping("/books")
//    public String books() { return "books.html"; }
//
//    @GetMapping("/books/{id}")
//    public String books_id(@PathVariable(name = "id") Long id, Model model) {
//        model.addAttribute("book_id", id);
//        return "book.html";
//    }


    @GetMapping("/activations")
    public String activations(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR) { return "forbidden.html"; }

        return "activations.html";
    }

    @GetMapping("/manage")
    public String manage(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR) { return "forbidden.html"; }

        return "manage.html";
    }

    @GetMapping("/manage_author")
    public String authorManage(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.AUTHOR) { return "forbidden.html"; }

        return "manage_author.html";
    }

    @GetMapping("/add/books")
    public String addBook(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR ) { return "forbidden.html"; }

        return "add_books.html";
    }

    @GetMapping("/add/books_author")
    public String authorAddBook(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.AUTHOR ) { return "forbidden.html"; }

        return "add_books_author.html";
    }

    @GetMapping("/add/genres")
    public String addGenre(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR) { return "forbidden.html"; }

        return "add_genres.html";
    }

    @GetMapping("/add/authors")
    public String addAuthor(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR) { return "forbidden.html"; }

        return "add_authors.html";
    }

    @GetMapping("/edit/books")
    public String editBooks(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR) { return "forbidden.html"; }

        return "edit_books.html";
    }

    @GetMapping("/edit/books_author")
    public String authorEditBooks(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.AUTHOR) { return "forbidden.html"; }

        return "edit_books_author.html";
    }

    @GetMapping("/edit/authors")
    public String editAuthors(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null || user.getAccountRole() != Account_Role.ADMINISTRATOR) { return "forbidden.html"; }

        return "edit_authors.html";
    }

    @GetMapping("/update")
    public String update(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) { return "error.html"; }

        return "update.html";
    }


}
