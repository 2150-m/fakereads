package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Polica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String naziv;

    @Column
    protected boolean primarna;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "POLICA_STAVKE_POVEZIVANJE",
            joinColumns = @JoinColumn(name = "polica_id"),
            inverseJoinColumns = @JoinColumn(name = "stavka_police_id")
    )
    protected Set<StavkaPolice> stavkaPolice = new HashSet<>();
}
