package wpproject.project.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import wpproject.project.repository.KorisnikRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Autor extends Korisnik {
    @Column
    protected boolean nalogAktivan;

    @ManyToMany
    @JoinTable(name = "AUTORI_KNJIGE",
            joinColumns = @JoinColumn(name = "autor_id"),
            inverseJoinColumns = @JoinColumn(name = "knjiga_id")
    )
    protected List<Knjiga> spisakKnjiga;

    public boolean isNalogAktivan() {
        return nalogAktivan;
    }

    public void setNalogAktivan(boolean nalogAktivan) {
        this.nalogAktivan = nalogAktivan;
    }

    public List<Knjiga> getSpisakKnjiga() {
        return spisakKnjiga;
    }

    public void setSpisakKnjiga(List<Knjiga> spisakKnjiga) {
        this.spisakKnjiga = spisakKnjiga;
    }
}
