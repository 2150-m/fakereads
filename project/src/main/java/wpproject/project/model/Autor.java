package wpproject.project.model;

import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
public class Autor extends Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    boolean nalogAktivan;

    @Column
    ArrayList<Knjiga> spisakKnjiga;
}
