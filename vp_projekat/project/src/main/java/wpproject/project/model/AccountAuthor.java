package wpproject.project.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AccountAuthor extends AccountUser {
    @Column
    protected boolean accountActivated;

    @ManyToMany
    @JoinTable(name = "AUTHORS_BOOKS",
            joinColumns = @JoinColumn(name = "account_author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    protected List<Book> books;

    public boolean isAccountActivated() {
        return accountActivated;
    }

    public void setAccountActivated(boolean accountActivated) {
        this.accountActivated = accountActivated;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
