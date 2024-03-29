package wpproject.project.dto;

public class DTO_Post_AccountRegister {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String mailAddress;
    private String password;

    public DTO_Post_AccountRegister(String firstName, String lastName, String username, String mailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.mailAddress = mailAddress;
        this.password = password;
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

}
