package wpproject.project.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
    protected Date reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AccountUser accountUser;

    public Long getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public AccountUser getAccountUser() {
        return accountUser;
    }
}
