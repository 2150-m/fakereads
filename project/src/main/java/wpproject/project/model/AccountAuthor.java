package wpproject.project.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
public class AccountAuthor extends Account {
    @Column
    protected boolean accountActivated = false;

    @ManyToMany
    @JoinTable(name = "AUTHORS_BOOKS",
            joinColumns = @JoinColumn(name = "account_author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    protected List<Book> books = new ArrayList<>();

    public AccountAuthor() {}

    public AccountAuthor(boolean accountActivated) {
        this.accountActivated = accountActivated;
    }

    public AccountAuthor(Account account) {
        super(account);
        this.accountActivated = false;
    }

    public boolean isAccountActivated() {
        return accountActivated;
    }
    public void setAccountActivated(boolean accountActivated) {
        this.accountActivated = accountActivated;
    }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}
