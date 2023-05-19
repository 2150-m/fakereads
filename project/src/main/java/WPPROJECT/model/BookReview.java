package WPPROJECT.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class BookReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected int rating;

    @Column
    protected String text;

    @Column
    protected Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AccountUser accountUser;
}
