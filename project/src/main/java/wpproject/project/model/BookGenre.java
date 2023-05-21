package wpproject.project.model;

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

    @ManyToMany(mappedBy = "bookGenres")
    private Set<Book> books;

    public BookGenre() {
    }

    public BookGenre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Zanr{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
