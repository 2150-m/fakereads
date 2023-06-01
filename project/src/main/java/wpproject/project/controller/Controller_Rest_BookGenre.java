package wpproject.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wpproject.project.dto.DTO_BookGenre;
import wpproject.project.model.BookGenre;
import wpproject.project.service.BookGenreService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller_Rest_BookGenre {
    @Autowired
    private BookGenreService genreService;

    @GetMapping("/api/database/genres")
    public ResponseEntity<List<DTO_BookGenre>> getGenres(HttpSession session) {
        List<BookGenre> genreList = genreService.findAll();

        BookGenre genre = (BookGenre) session.getAttribute("genre");
        if (genre == null) { System.out.println("No session"); }
        else               { System.out.println(genre);        }

        List<DTO_BookGenre> dtos = new ArrayList<>();
        for (BookGenre g : genreList) { DTO_BookGenre dto = new DTO_BookGenre(g); dtos.add(dto); }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/api/database/genre/{id}")
    public BookGenre getGenre(@PathVariable(name = "id") Long id, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return genreService.findOne(id);
    }

    @GetMapping("/api/database/genre/name={name}")
    public BookGenre getGenre(@PathVariable(name = "name") String name, HttpSession session) {
        BookGenre genre = (BookGenre) session.getAttribute("genre");
        System.out.println(genre);
        session.invalidate();
        return genreService.findOne(name);
    }


}
