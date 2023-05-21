package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
public class ShelfItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SHELF_ITEM_REVIEW",
            joinColumns = @JoinColumn(name = "shelf_item_id"),
            inverseJoinColumns = @JoinColumn(name = "book_review_id")
    )
    protected Set<BookReview> bookReviews;

    @ManyToOne
    protected Book book;

    public ShelfItem() {}

    public ShelfItem(Book book) {
        this.book = book;
    }
}