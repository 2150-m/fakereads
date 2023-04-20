package wpproject.project.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CollectionIdMutability;

import java.io.Serializable;

@Entity
public class Recenzija implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected int ocena;

    @Column
    protected String tekst;

    @Column
    protected String datumRecenzije;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Korisnik korisnik;
}
