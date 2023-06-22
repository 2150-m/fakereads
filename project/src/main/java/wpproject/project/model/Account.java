package wpproject.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import wpproject.project.configuration.DatabaseConfiguration;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private  String username;

    @Column(unique = true)
    private String mailAddress;

    @Column
    private String password;

    @Temporal(TemporalType.DATE)
    @Column
    private LocalDate dateOfBirth;

    @Column
    private String profilePicture;

    @Column
    private String description;

    @Column
    private Account_Role accountRole;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ACCOUNTS_SHELVES",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "shelf_id", referencedColumnName = "id")
    )
    private List<Shelf> shelves = new ArrayList<>();

    public Account() {
        this("", "", "", "", "", LocalDate.MIN, "default.jpg", "bio", Account_Role.READER);
    }

    public Account(String firstName, String lastName, String username, String mailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.mailAddress = mailAddress;
        this.password = password;
        this.dateOfBirth = LocalDate.of(1970, 1, 1);
        this.profilePicture = "/avatars/default.jpg";
        this.description = "bio";
        this.accountRole = Account_Role.READER;

        Shelf shelf_WantToRead = new Shelf("WantToRead", true);
        Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
        Shelf shelf_Read = new Shelf("Read", true);
        shelves.addAll(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));
    }

    public Account(Account account) {
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

    public Account(String firstName, String lastName, String username, String mailAddress, String password, LocalDate dateOfBirth, String profilePicture, String description, Account_Role accountRole) {
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

//    @JsonIgnore
    public List<Shelf> getShelves() {
        return shelves;
    }
    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", profilePicture='" + profilePicture + '\'' +
                ", description='" + description + '\'' +
                ", accountRole=" + accountRole +
                //", shelves=" + shelves +
                '}';
    }
}
