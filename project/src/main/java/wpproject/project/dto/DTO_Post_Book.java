package wpproject.project.dto;

import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DTO_Post_Book {
    private String title;
    private String coverPhoto;
    private LocalDate releaseDate;
    private String description;
    private int numOfPages;
    private String isbn;
    private List<String> genreNames = new ArrayList<>();

    public DTO_Post_Book() {}

    public DTO_Post_Book(String title, String coverPhoto, LocalDate releaseDate, String description, int numOfPages, String isbn) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.releaseDate = releaseDate;
        this.description = description;
        this.numOfPages = numOfPages;
        this.isbn = isbn;
    }

    public DTO_Post_Book(String title, String coverPhoto, LocalDate releaseDate, String description, int numOfPages, String isbn, List<String> genreNames) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.releaseDate = releaseDate;
        this.description = description;
        this.numOfPages = numOfPages;
        this.isbn = isbn;
        this.genreNames = genreNames;
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

    public List<String> getGenreNames() {
        return genreNames;
    }

    public void setGenres(List<String> genreNames) {
        this.genreNames = genreNames;
    }
}
