package wpproject.project.dto;

import wpproject.project.model.Account;

public class AccountLoginDTO {

    private Long id;
    private String username;
    private String password;

    public AccountLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
