package WPPROJECT.model;

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
    protected String coverPicture;

    @Column
    protected Date releaseDate;

    @Column
    protected String description;


    @ManyToMany
    @JoinTable(name = "KNJIGE_ZANROVI",
            joinColumns = @JoinColumn(name = "knjiga_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "zanr_id", referencedColumnName = "id"))
    private Set<BookGenre> genres = new HashSet<>();

    @Column
    protected int numberOfPages;

    @Column
    protected double rating;

    @Column
    protected String ISBN;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverPicture='" + coverPicture + '\'' +
                ", releaseDate=" + releaseDate +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", numberOfPages=" + numberOfPages +
                ", rating=" + rating +
                ", ISBN='" + ISBN + '\'' +
                '}';
    }
}
