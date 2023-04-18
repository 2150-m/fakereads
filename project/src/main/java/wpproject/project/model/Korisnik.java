package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Korisnik {
    @Column
    String ime;

    @Column
    String prezime;

    @Column(unique = true)
    String korisnickoIme;

    @Column(unique = true)
    String mejlAdresa;

    @Column
    String lozinka;

    @Column
    String datumRodjenja;

    @Column
    String profilnaSlika;

    @Column
    String opis;

    enum Uloga { CITALAC, ADMINISTRATOR, AUTOR };
}
