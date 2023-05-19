package WPPROJECT.model;

import jakarta.persistence.*;

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
    protected String firstname;

    @Column
    protected String lastname;

    @Column(unique = true)
    protected String username;

    @Column(unique = true)
    protected String email;

    @Column
    protected String password;

    @Column
    protected Date dateOfBirth;

    @Column
    protected String profilePicture;

    @Column
    protected String bio;

    @Column
    protected AccountUser_Role accountRole;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected Set<Shelf> shelfs = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password;}
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) {this.dateOfBirth = dateOfBirth; }
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public AccountUser_Role getAccountRole() { return accountRole; }
    public void setAccountRole(AccountUser_Role accountRole) { this.accountRole = accountRole; }
    public Set<Shelf> getShelfs() { return shelfs; }
    public void setShelfs(Set<Shelf> shelfs) { this.shelfs = shelfs; }

    @Override
    public String toString() {
        return "AccountUser{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", profilePicture='" + profilePicture + '\'' +
                ", bio='" + bio + '\'' +
                ", accountRole=" + accountRole +
                ", shelfs=" + shelfs +
                '}';
    }
}
