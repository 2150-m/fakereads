package wpproject.project.dto;

import wpproject.project.model.*;

import java.util.ArrayList;
import java.util.List;

public class DTO_View_AccountAuthor extends DTO_View_Account {
    private boolean acccountActivated;
    private List<Book> authorsBooks = new ArrayList<>();

    public DTO_View_AccountAuthor(Account account, AccountAuthor author) {
        super(account);
        this.acccountActivated = author.isAccountActivated();
        this.authorsBooks = author.getBooks();
    }

    public boolean isAcccountActivated() { return acccountActivated; }
    public void setAcccountActivated(boolean acccountActivated) { this.acccountActivated = acccountActivated; }
    public List<Book> getAuthorsBooks() { return authorsBooks; }
    public void setAuthorsBooks(List<Book> authorsBooks) { this.authorsBooks = authorsBooks; }
}
