package wpproject.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class BookReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double rating;

    @Column
    private String text;

    @Temporal(TemporalType.DATE)
    @Column
    private LocalDate reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    public BookReview() {}

    public BookReview(double rating, String text, LocalDate reviewDate, Account account) {
        this.rating = rating;
        this.text = text;
        this.reviewDate = reviewDate;
        this.account = account;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
    @JsonIgnore
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
