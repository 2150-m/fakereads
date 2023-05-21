package wpproject.project.model;

import jakarta.persistence.*;

import java.util.Set;


@Entity
public class AccountAuthor extends AccountUser {
    @Column
    protected boolean accountActivated;

    @ManyToMany
    @JoinTable(name = "AUTHORS_BOOKS",
            joinColumns = @JoinColumn(name = "account_author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    protected Set<Book> books;

    public AccountAuthor() {}

    public AccountAuthor(boolean accountActivated) {
        this.accountActivated = accountActivated;
    }

    public boolean isAccountActivated() {
        return accountActivated;
    }

    public void setAccountActivated(boolean accountActivated) {
        this.accountActivated = accountActivated;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
