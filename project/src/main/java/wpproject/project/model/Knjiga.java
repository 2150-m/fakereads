package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Knjiga {
    @Column(name = "nasolov")
    String naslov;

    @Column
    String naslovnaFotografija;

    @Column
    String datumObjavljivanja;

    @Column
    String opis;

    @Column
    Zanr zanr;

    @Column
    int brojStrana;

    @Column
    int ocena;

    @Column
    long isbn;
}
