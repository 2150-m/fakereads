package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.BookDTO;
import wpproject.project.dto.ShelfItemDTO;
import wpproject.project.model.Account;
import wpproject.project.model.Book;
import wpproject.project.model.Shelf;
import wpproject.project.model.ShelfItem;
import wpproject.project.service.AccountService;
import wpproject.project.service.BookService;
import wpproject.project.service.ShelfItemService;
import wpproject.project.service.ShelfService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class ShelfItemRestController {
    @Autowired
    private ShelfItemService shelfItemService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ShelfService shelfService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/database/books")
    public ResponseEntity<List<ShelfItemDTO>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = shelfItemService.findAll();

        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        if (item == null) { System.out.println("No session"); }
        else              { System.out.println(item);         }

        List<ShelfItemDTO> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) { ShelfItemDTO dto = new ShelfItemDTO(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/book/{id}")
    public ShelfItem getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return shelfItemService.findOne(id);
    }

    @GetMapping("/api/database/book/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return shelfItemService.findByBook(bookService.findOne(title));
    }

    @GetMapping("/api/database/book/search={search}")
    public ResponseEntity<List<ShelfItemDTO>> searchItems(@PathVariable(name = "search") String search, HttpSession session) {
        List<ShelfItem> items = shelfItemService.findAll();

        List<ShelfItemDTO> dtos = new ArrayList<>();
        for (ShelfItem i : items) {
            // search by title / description
            if (i.getBook().getTitle().contains(search) || i.getBook().getDescription().contains(search)) {
                ShelfItemDTO dto = new ShelfItemDTO(i); dtos.add(dto);
            }
        }

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/api/database/book/add")
    public ResponseEntity<String> addItem(@RequestBody BookDTO bookDTO, HttpSession session) {
        try {
            if (bookService.findByIsbn(bookDTO.getIsbn()) != null) {
                return ResponseEntity.badRequest().body("A book with the same ISBN (" + bookDTO.getIsbn() + ") is already in the database.");
            }

            Book book = new Book(bookDTO.getTitle(), bookDTO.getCoverPhoto(), LocalDate.now(), bookDTO.getDescription(), bookDTO.getNumOfPages(), 0, bookDTO.getIsbn());
            bookService.save(book);

            ShelfItem item = new ShelfItem(book);
            shelfItemService.save(item);

            return ResponseEntity.ok(book.getTitle().toUpperCase() + " (" + book.getId() + ") has been added to the database.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the item: " + e.getMessage());
        }
    }

    @PostMapping("/api/user/add/book/{bookId}/shelf/{shelfName}")
    public ResponseEntity userAddBook(@PathVariable(name = "bookId") Long bookId, @PathVariable(name = "shelfName") String shelfName, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("You have to be logged in to add a book to a shelf.");
        }
        user = accountService.findOne(user.getId());

        ShelfItem targetItem = shelfItemService.findByBook(bookService.findOne(bookId));
        if (targetItem == null) {
            return ResponseEntity.badRequest().body("Book with this ID does not exist.");
        }

        Shelf targetShelf = null;
        for (Shelf userShelf : user.getShelves()) {
            if (userShelf.getName().equalsIgnoreCase(shelfName)) {
                targetShelf = userShelf;
                break;
            }
        }

        if (targetShelf == null) {
            return ResponseEntity.badRequest().body("Shelf " + shelfName.toUpperCase() + " does not exist.");
        }

        // contains() and object.equals(otherobject) don't work
        for (ShelfItem item : targetShelf.getShelfItems()) {
            if (item.getId().equals(targetItem.getId())) {
                return ResponseEntity.badRequest().body("This book is already on " + targetShelf.getName().toUpperCase() + ".");
            }
        }

        if (targetShelf.isPrimary()) {
            // Remove the book from the other primary shelf (if it's on it)
            for (Shelf shelf : user.getShelves().subList(0, 3)) {
                if (shelf.getId().equals(targetShelf.getId())) { continue; }

                Iterator<ShelfItem> iterator = shelf.getShelfItems().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId().equals(targetItem.getId())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            targetShelf.getShelfItems().add(targetItem);
            shelfService.save(targetShelf);
            return ResponseEntity.ok().body(targetItem.getBook().getTitle().toUpperCase() + " (" + targetItem.getBook().getId() + ") has been added to " + targetShelf.getName().toUpperCase() + " (" + targetShelf.getId() + ").");
        }
        
        // Check if the item is in a primary shelf
        for (Shelf shelf : user.getShelves().subList(0, 3)) {
            for (ShelfItem item : shelf.getShelfItems()) {
                if (item.getId().equals(targetItem.getId())) {
                    targetShelf.getShelfItems().add(targetItem);
                    shelfService.save(targetShelf);
                    return ResponseEntity.ok().body(targetItem.getBook().getTitle().toUpperCase() + "(" + targetItem.getBook().getId() + ") has been added to " + targetShelf.getName().toUpperCase() + " (" + targetShelf.getId() + ").");
                }
            }
        }

        return ResponseEntity.badRequest().body("An item has to be on a primary shelf in order to be added to custom ones.");
    }

    // TODO: Add book by ISBN
}
