package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class StavkaPolice implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected Recenzija recenzija;

    @Column
    protected Knjiga knjiga;
}
