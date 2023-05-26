package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wpproject.project.dto.BookDTO;
import wpproject.project.dto.ShelfItemDTO;
import wpproject.project.model.Book;
import wpproject.project.model.ShelfItem;
import wpproject.project.service.BookService;
import wpproject.project.service.ShelfItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShelfItemRestController {
    @Autowired
    private ShelfItemService shelfItemService;
    @Autowired
    private BookService bookService;

    @GetMapping("/api/items")
    public ResponseEntity<List<ShelfItemDTO>> getItems(HttpSession session) {
        List<ShelfItem> shelfItems = shelfItemService.findAll();

        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        if (item == null) { System.out.println("No session"); }
        else              { System.out.println(item);         }

        List<ShelfItemDTO> dtos = new ArrayList<>();
        for (ShelfItem b : shelfItems) { ShelfItemDTO dto = new ShelfItemDTO(b); dtos.add(dto); }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/item/{id}")
    public ShelfItem getItem(@PathVariable(name = "id") Long id, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return shelfItemService.findOne(id);
    }

    @GetMapping("/api/item/title={title}")
    public ShelfItem getItem(@PathVariable(name = "title") String title, HttpSession session) {
        ShelfItem item = (ShelfItem) session.getAttribute("shelfItem");
        System.out.println(item);
        session.invalidate();
        return shelfItemService.findByBook(bookService.findOne(title));
    }

    @GetMapping("/api/item/search={search}")
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

    @PostMapping("/api/item/add")
    public ResponseEntity addItem(@RequestBody Book book) {
        try {
            ShelfItem item = shelfItemService.findByBook(book);
            if (item != null) { return ResponseEntity.badRequest().body("This item (book) is already in the database."); }

            item = new ShelfItem(book);
            shelfItemService.save(item);

            return ResponseEntity.ok("Item (book) [id: " + book.getId() + " / title: " + book.getTitle() + "] has been added to the database.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add the item: " + e.getMessage());
        }
    }
}
