package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class BookReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected double rating;

    @Column
    protected String text;

    @Temporal(TemporalType.DATE)
    @Column
    protected LocalDate reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Account account;

    public BookReview() {}

    public BookReview(double rating, String text, LocalDate reviewDate, Account account) {
        this.rating = rating;
        this.text = text;
        this.reviewDate = reviewDate;
        this.account = account;
    }

    public Long getId() {
        return id;
    }
    public double getRating() {
        return rating;
    }
    public String getText() {
        return text;
    }
    public LocalDate getReviewDate() {
        return reviewDate;
    }
    public Account getAccountUser() {
        return account;
    }
}
