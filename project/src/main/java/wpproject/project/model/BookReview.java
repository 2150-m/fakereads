package wpproject.project.model;

import jakarta.persistence.*;

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

    @Column
    protected Date reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AccountUser accountUser;
}
