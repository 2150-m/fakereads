package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class StavkaPolice {

    @Column
    Recenzija recenzija;

    @Column
    Knjiga knjiga;
}
