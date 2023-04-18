package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Recenzija {


    @Column
    int ocena;

    @Column
    String tekst;

    @Column
    String datumRecenzije;

    @Column
    Korisnik korisnik;


}
