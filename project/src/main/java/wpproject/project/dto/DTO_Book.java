package wpproject.project.dto;

import wpproject.project.model.Book;

import java.time.LocalDate;

public class DTO_Book {
    private Long id;
    private String title;
    private String coverPhoto;
    private LocalDate releaseDate;
    private String description;
    private int numOfPages;
    private String isbn;

    public DTO_Book() {}

    public DTO_Book(String title, String coverPhoto, LocalDate releaseDate, String description, int numOfPages, String isbn) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.releaseDate = releaseDate;
        this.description = description;
        this.numOfPages = numOfPages;
        this.isbn = isbn;
    }

    public DTO_Book(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.coverPhoto = book.getCoverPhoto();
        this.releaseDate = book.getReleaseDate();
        this.description = book.getDescription();
        this.numOfPages = book.getNumOfPages();
        this.isbn = book.getIsbn();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
}
