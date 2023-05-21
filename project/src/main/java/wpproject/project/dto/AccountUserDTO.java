package wpproject.project.dto;

import wpproject.project.model.AccountUser;
import wpproject.project.model.Shelf;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String mailAddress;
    private String password;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String description;
    private AccountUser.AccountRole accountRole;
    private Set<Shelf> shelves = new HashSet<>();

    public AccountUserDTO() {

    }

    public AccountUserDTO(AccountUser user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.mailAddress = user.getMailAddress();
        this.password = user.getPassword();
        this.dateOfBirth = user.getDateOfBirth();
        this.profilePicture = user.getProfilePicture();
        this.description = user.getDescription();
        this.accountRole = user.getAccountRole();
        this.shelves = user.getShelves();
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

    public AccountUser.AccountRole getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(AccountUser.AccountRole accountRole) {
        this.accountRole = accountRole;
    }

    public Set<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(Set<Shelf> shelves) {
        this.shelves = shelves;
    }
}
