package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected int rating;

    @Column
    protected String text;

    @Column
    protected String reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AccountUser accountUser;
}
