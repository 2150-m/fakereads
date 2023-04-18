package wpproject.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

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

    enum Status { CEKANJE, ODOBREN, ODBIJEN }

    @Column
    Status status;
}
