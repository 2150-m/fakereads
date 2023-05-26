package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.BookGenreDTO;
import wpproject.project.model.BookGenre;
import wpproject.project.service.BookGenreService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookGenreRestController {
    @Autowired
    private BookGenreService genreService;

    @GetMapping("/api/genres")
    public ResponseEntity<List<BookGenreDTO>> getGenres(HttpSession session) {
        List<BookGenre> genreList = genreService.findAll();

        BookGenre genre = (BookGenre) session.getAttribute("genre");
        if (genre == null) { System.out.println("No session"); }
        else               { System.out.println(genre);        }

        List<BookGenreDTO> dtos = new ArrayList<>();
        for (BookGenre g : genreList) { BookGenreDTO dto = new BookGenreDTO(g); dtos.add(dto); }

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
