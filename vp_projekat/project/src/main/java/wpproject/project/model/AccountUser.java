package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

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

    @Column
    protected String dateOfBirth;

    @Column
    protected String profilePicture;

    @Column
    protected String description;

    enum AccountRole {READER, AUTHOR, ADMINISTRATOR }

    @Column
    protected AccountRole accountRole;

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

    public String getDateOfBirth() {
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

    public void setDateOfBirth(String dateOfBirth) {
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
}
