package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class StavkaPolice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "stavka_police_recenzija",
            joinColumns = @JoinColumn(name = "stavka_police_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzija_id")
    )
    protected List<Recenzija> recenzija = new ArrayList<>();

    @OneToOne
    protected Knjiga knjiga;
}