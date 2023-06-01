package wpproject.project.dto;

import wpproject.project.model.Account;
import wpproject.project.model.Account_Role;

import java.time.LocalDate;

public class AccountDTO_NoShelves {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String mailAddress;
    private String password;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String description;
    private Account_Role accountRole;

    public AccountDTO_NoShelves() {}

    public AccountDTO_NoShelves(Account account) {
        this.id = account.getId();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.username = account.getUsername();
        this.mailAddress = account.getMailAddress();
        this.password = account.getPassword();
        this.dateOfBirth = account.getDateOfBirth();
        this.profilePicture = account.getProfilePicture();
        this.description = account.getDescription();
        this.accountRole = account.getAccountRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Account_Role getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(Account_Role accountRole) {
        this.accountRole = accountRole;
    }
}
