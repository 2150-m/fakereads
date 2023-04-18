package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Korisnik implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String ime;

    @Column
    protected String prezime;

    @Column(unique = true)
    protected  String korisnickoIme;

    @Column(unique = true)
    protected String mejlAdresa;

    @Column
    protected String lozinka;

    @Column
    protected String datumRodjenja;

    @Column
    protected String profilnaSlika;

    @Column
    protected String opis;

    @Column
    protected String uloga;
}
