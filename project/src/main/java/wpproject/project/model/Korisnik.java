package wpproject.project.model;

import jakarta.persistence.Column;

public class Korisnik {
    String ime;
    String prezime;
    @Column(unique = true)
    String korisnickoIme;
    @Column(unique = true)
    String mejlAdresa;
    String lozinka;
    String datumRodjenja;
    String profilnaSlika;
    String opis;
    enum Uloga { CITALAC, ADMINISTRATOR, AUTOR };
}
