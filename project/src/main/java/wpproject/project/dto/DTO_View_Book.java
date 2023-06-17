package wpproject.project.dto;

import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DTO_View_Book {
    private String title;
    private String coverPhoto;
    private LocalDate releaseDate;
    private String description;
    private int numOfPages;
    private String isbn;
    private double rating;
    private List<BookGenre> genres = new ArrayList<>();

    public DTO_View_Book() {}

    public DTO_View_Book(Book book) {
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.releaseDate = book.getReleaseDate();
        this.description = book.getDescription();
        this.numOfPages = book.getNumOfPages();
        this.isbn = book.getIsbn();
        this.genres = book.getBookGenres();
        this.rating = book.getRating();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(String coverPhoto) { this.coverPhoto = coverPhoto; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getNumOfPages() { return numOfPages; }
    public void setNumOfPages(int numOfPages) { this.numOfPages = numOfPages; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public List<BookGenre> getGenreNames() {
        return genres;
    }
    public void setGenres(List<BookGenre> genres) {
        this.genres = genres;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public List<BookGenre> getGenres() {
        return genres;
    }
}
