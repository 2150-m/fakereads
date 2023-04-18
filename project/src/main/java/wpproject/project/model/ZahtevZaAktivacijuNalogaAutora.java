package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class ZahtevZaAktivacijuNalogaAutora implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String email;

    @Column
    protected String telefon;

    @Column
    protected String poruka;

    @Column
    protected String datum;

    enum Status { CEKANJE, ODOBREN, ODBIJEN }

    @Column
    protected Status status;
}
