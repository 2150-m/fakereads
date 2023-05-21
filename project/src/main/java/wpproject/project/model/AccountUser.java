package wpproject.project.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AccountUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String firstName;

    @Column
    protected String lastName;

    @Column(unique = true)
    protected  String username;

    @Column(unique = true)
    protected String mailAddress;

    @Column
    protected String password;

    @Temporal(TemporalType.DATE)
    @Column
    protected Date dateOfBirth;

    @Column
    protected String profilePicture;

    @Column
    protected String description;

    public enum AccountRole {READER, AUTHOR, ADMINISTRATOR }

    @Column
    protected AccountRole accountRole;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected Set<Shelf> shelves = new HashSet<>();

    public AccountUser() {}

    public AccountUser(String firstName, String lastName, String username, String mailAddress, String password, Date dateOfBirth, String profilePicture, String description, AccountRole accountRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.mailAddress = mailAddress;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.profilePicture = profilePicture;
        this.description = description;
        this.accountRole = accountRole;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(AccountRole accountRole) {
        this.accountRole = accountRole;
    }

    public Set<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(Set<Shelf> shelves) {
        this.shelves = shelves;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getDescription() {
        return description;
    }

    public AccountRole getUloga() {
        return accountRole;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUloga(AccountRole accountRole) {
        this.accountRole = accountRole;
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", ime='" + firstName + '\'' +
                ", prezime='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", description='" + description + '\'' +
                ", uloga=" + accountRole +
                '}';
    }
}
