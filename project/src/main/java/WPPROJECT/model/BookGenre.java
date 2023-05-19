package WPPROJECT.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BookGenre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String name;

    @ManyToMany(mappedBy = "zanrovi")
    private Set<Book> books = new HashSet<>();

    @Override
    public String toString() {
        return "BookGenre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
