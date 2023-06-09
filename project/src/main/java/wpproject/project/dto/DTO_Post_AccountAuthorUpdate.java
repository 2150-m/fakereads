package wpproject.project.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import wpproject.project.model.Account;
import wpproject.project.model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DTO_Post_AccountAuthorUpdate {
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String description;
    private boolean accountActivated;

    public DTO_Post_AccountAuthorUpdate() {}

    public DTO_Post_AccountAuthorUpdate(Account account, boolean accountActivated) {
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.username = account.getUsername();
        this.dateOfBirth = account.getDateOfBirth();
        this.profilePicture = account.getProfilePicture();
        this.description = account.getDescription();
        this.accountActivated = accountActivated;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccountActivated() {
        return accountActivated;
    }

    public void setAccountActivated(boolean accountActivated) {
        this.accountActivated = accountActivated;
    }
}
