package WPPROJECT.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
public class ShelfItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "stavka_police_recenzija",
            joinColumns = @JoinColumn(name = "stavka_police_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzija_id")
    )
    protected Set<BookReview> reviews;

    @ManyToOne
    protected Book book;
}