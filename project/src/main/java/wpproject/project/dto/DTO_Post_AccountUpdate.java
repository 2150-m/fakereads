package wpproject.project.dto;

import java.time.LocalDate;

public class DTO_Post_AccountUpdate {
    private String firstName;
    private String lastName;
    private String username;
    private String mailAddress;
    private String password;
    private String newPassword;
    private LocalDate dateOfBirth;
    private String profilePicture;
    private String description;

    public DTO_Post_AccountUpdate() {}

    public DTO_Post_AccountUpdate(String firstName, String lastName, String username, String mailAddress, String password, String newPassword, LocalDate dateOfBirth, String profilePicture, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.mailAddress = mailAddress;
        this.password = password;
        this.newPassword = newPassword;
        this.dateOfBirth = dateOfBirth;
        this.profilePicture = profilePicture;
        this.description = description;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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
}
