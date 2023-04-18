package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

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

    @Column
    protected Zanr zanr;

    @Column
    protected int brojStrana;

    @Column
    protected int ocena;

    @Column
    protected long isbn;
}
