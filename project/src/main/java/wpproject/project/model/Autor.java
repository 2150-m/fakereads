package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Autor extends Korisnik implements Serializable {

    @Column
    protected boolean nalogAktivan;

    @Column
    protected String spisakKnjiga;
}
