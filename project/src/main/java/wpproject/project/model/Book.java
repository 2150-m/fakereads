package wpproject.project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String coverPhoto;

    @Temporal(TemporalType.DATE)
    @Column
    private LocalDate releaseDate;

    @Column
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "BOOKS_GENRES",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_genre_id", referencedColumnName = "id"))
//    @JsonManagedReference
    private List<BookGenre> bookGenres = new ArrayList<>();

    @Column
    private int numOfPages;

    @Column
    private double rating;

    @Column
    private String isbn;

    public Book() {}

    public Book(String title, String coverPhoto, LocalDate releaseDate, String description, int numOfPages, double rating, String isbn) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.releaseDate = releaseDate;
        this.description = description;
        this.numOfPages = numOfPages;
        this.rating = rating;
        this.isbn = isbn;
    }

    public Book(String title, String coverPhoto, LocalDate releaseDate, String description, int numOfPages, double rating, String isbn, List<BookGenre> genres) {
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.releaseDate = releaseDate;
        this.description = description;
        this.numOfPages = numOfPages;
        this.rating = rating;
        this.isbn = isbn;
        this.bookGenres = genres;
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

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverPhoto='" + coverPhoto + '\'' +
                ", releaseDate=" + releaseDate +
                ", description='" + description + '\'' +
                ", bookGenres=" + bookGenres +
                ", numOfPages=" + numOfPages +
                ", rating=" + rating +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
