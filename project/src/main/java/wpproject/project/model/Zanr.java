package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Zanr implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String naziv;
}
