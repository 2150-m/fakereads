package wpproject.project.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import wpproject.project.configuration.DatabaseConfiguration;
import wpproject.project.repository.ShelfRepository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account implements Serializable {
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
    protected LocalDate dateOfBirth;

    @Column
    protected String profilePicture;

    @Column
    protected String description;

    @Column
    protected Account_Role accountRole;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    /*@JoinTable(name = "ACCOUNTS_SHELVES",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "shelf_id", referencedColumnName = "id")
    )*/
    protected List<Shelf> shelves = new ArrayList<>();

//    @Autowired
//    private DatabaseConfiguration dbConfig;

    public Account() {
        this("", "", "", "", "", LocalDate.MIN, "", "", Account_Role.READER);
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

        List<Shelf> defaultShelves = new ArrayList<>();
        defaultShelves = DatabaseConfiguration.DefaultShelves();
        this.shelves.addAll(defaultShelves);

//        Shelf shelf_WantToRead = new Shelf("WantToRead", true);
//        Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
//        Shelf shelf_Read = new Shelf("Read", true);
//        List<Shelf> defaultShelves = List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read);
//
//        this.shelves.addAll(defaultShelves);
//        dbConfig.SaveDefaultShelves(defaultShelves);
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

    public List<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
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
