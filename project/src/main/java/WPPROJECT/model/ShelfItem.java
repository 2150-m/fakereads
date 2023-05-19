package WPPROJECT.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    protected List<BookReview> reviews = new ArrayList<>();

    @ManyToOne
    protected Book book;
}