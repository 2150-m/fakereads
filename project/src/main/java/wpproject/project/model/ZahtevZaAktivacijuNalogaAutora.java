package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;


enum Status { CEKANJE, ODOBREN, ODBIJEN }

@Entity
public class ZahtevZaAktivacijuNalogaAutora {

    @Column
    String email;

    @Column
    String telefon;

    @Column
    String poruka;

    @Column
    String datum;

    @Column
    Status status;
}
