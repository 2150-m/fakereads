package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Book> books = new HashSet<>();

    @Override
    public String toString() {
        return "Zanr{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
