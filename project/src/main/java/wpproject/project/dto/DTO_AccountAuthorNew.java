package wpproject.project.dto;

import wpproject.project.model.Account_Role;

import java.time.LocalDate;

public class DTO_AccountAuthorNew {

    private Long id;
    private String firstName;
    private String lastName;
    private  String username;
    private String mailAddress;
    private String password;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String description;
    private Account_Role accountRole;

    public DTO_AccountAuthorNew(
        String firstName,
        String lastName,
        String username,
        String mailAddress,
        String password,
        LocalDate dateOfBirth,
        String profilePicture,
        String description,
        Account_Role accountRole
    ) {
        this.id = id;
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMailAddress() { return mailAddress; }
    public void setMailAddress(String mailAddress) { this.mailAddress = mailAddress; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Account_Role getAccountRole() { return accountRole; }
    public void setAccountRole(Account_Role accountRole) {this.accountRole = accountRole; }
}
