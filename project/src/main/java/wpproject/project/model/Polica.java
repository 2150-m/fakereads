package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Polica {

    @Column
    String naziv;

    @Column
    boolean primarna;

    @Column
    StavkaPolice stavkaPolice;
}
