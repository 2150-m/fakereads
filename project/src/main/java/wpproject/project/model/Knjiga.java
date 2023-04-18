package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Knjiga implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String naslov;

    @Column
    protected String naslovnaFotografija;

    @Column
    protected String datumObjavljivanja;

    @Column
    protected String opis;


    @ManyToMany
    @JoinTable(name = "KNJIGA_ZANROVI",
            joinColumns = @JoinColumn(name = "knjiga_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "zanr_id", referencedColumnName = "id"))
    private Set<Zanr> zanrovi = new HashSet<>();

    @Column
    protected int brojStrana;

    @Column
    protected int ocena;

    @Column
    protected long isbn;

    @Override
    public String toString() {
        return "Knjiga{" +
                "id=" + id +
                ", naslov='" + naslov + '\'' +
                ", naslovnaFotografija='" + naslovnaFotografija + '\'' +
                ", datumObjavljivanja='" + datumObjavljivanja + '\'' +
                ", opis='" + opis + '\'' +
                ", zanrovi=" + zanrovi +
                ", brojStrana=" + brojStrana +
                ", ocena=" + ocena +
                ", isbn=" + isbn +
                '}';
    }
}
