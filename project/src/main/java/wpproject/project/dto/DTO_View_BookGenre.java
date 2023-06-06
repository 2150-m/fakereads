package wpproject.project.dto;

import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;

import java.util.ArrayList;
import java.util.List;

public class DTO_View_BookGenre {
    protected Long id;
    protected String name;
    private List<Book> books = new ArrayList<>(); // If included, creates an infinite tree in the JSON view

    public DTO_View_BookGenre() {}

    public DTO_View_BookGenre(BookGenre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        this.books = genre.getBooks();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}
