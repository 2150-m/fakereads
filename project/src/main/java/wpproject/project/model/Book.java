package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String title;

    @Column
    protected String coverPhoto;

    @Column
    protected Date releaseDate;

    @Column
    protected String description;


    @ManyToMany
    @JoinTable(name = "BOOKS_GENRES",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_genre_id", referencedColumnName = "id"))
    private Set<BookGenre> bookGenres;

    @Column
    protected int numOfPages;

    @Column
    protected double rating;

    @Column
    protected long isbn;

    @Override
    public String toString() {
        return "Knjiga{" +
                "id=" + id +
                ", naslov='" + title + '\'' +
                ", naslovnaFotografija='" + coverPhoto + '\'' +
                ", datumObjavljivanja='" + releaseDate + '\'' +
                ", description='" + description + '\'' +
                ", zanrovi=" + bookGenres +
                ", brojStrana=" + numOfPages +
                ", rating=" + rating +
                ", isbn=" + isbn +
                '}';
    }
}
