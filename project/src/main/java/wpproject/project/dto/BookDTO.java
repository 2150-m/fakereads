package wpproject.project.dto;

import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookDTO {
    private Long id;
    private String title;
    private String coverPhoto;
    private LocalDate releaseDate;
    private String description;
    private List<BookGenre> bookGenres = new ArrayList<>();
    private int numOfPages;
    private double rating;
    private String isbn;

    public BookDTO() {}

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.releaseDate = book.getReleaseDate();
        this.description = book.getDescription();
        this.bookGenres = book.getBookGenres();
        this.numOfPages = book.getNumOfPages();
        this.rating = book.getRating();
        this.isbn = book.getIsbn();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BookGenre> getBookGenres() {
        return bookGenres;
    }

    public void setBookGenres(List<BookGenre> bookGenres) {
        this.bookGenres = bookGenres;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
