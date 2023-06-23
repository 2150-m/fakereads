package wpproject.project.dto;

import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;

import java.util.ArrayList;
import java.util.List;

public class DTO_View_BookGenreWithoutBooks {
    protected Long id;
    protected String name;
    public DTO_View_BookGenreWithoutBooks() {}

    public DTO_View_BookGenreWithoutBooks(BookGenre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
