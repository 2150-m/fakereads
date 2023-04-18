package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Polica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String naziv;

    @Column
    protected boolean primarna;

    @Column
    protected StavkaPolice stavkaPolice;
}
