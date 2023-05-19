package WPPROJECT.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AccountAuthor extends AccountUser {
    @Column
    protected boolean activated;

    @ManyToMany
    @JoinTable(name = "AUTORI_KNJIGE",
            joinColumns = @JoinColumn(name = "autor_id"),
            inverseJoinColumns = @JoinColumn(name = "knjiga_id")
    )
    protected List<Book> books;

    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
