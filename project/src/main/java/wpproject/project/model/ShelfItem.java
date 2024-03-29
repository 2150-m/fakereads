package wpproject.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShelfItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SHELF_ITEM_REVIEW",
            joinColumns = @JoinColumn(name = "shelf_item_id"),
            inverseJoinColumns = @JoinColumn(name = "book_review_id")
    )
//    @JsonBackReference
    private List<BookReview> bookReviews = new ArrayList<>();

    public ShelfItem() {}
    public ShelfItem(Book book) {
        this.book = book;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<BookReview> getBookReviews() {
        return bookReviews;
    }
    public void setBookReviews(List<BookReview> bookReviews) {
        this.bookReviews = bookReviews;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
}