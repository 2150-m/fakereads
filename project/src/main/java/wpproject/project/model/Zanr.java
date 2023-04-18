package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Zanr implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String naziv;

    @ManyToMany(mappedBy = "zanrovi")
    private Set<Knjiga> knjige = new HashSet<>();

    @Override
    public String toString() {
        return "Zanr{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                '}';
    }
}
