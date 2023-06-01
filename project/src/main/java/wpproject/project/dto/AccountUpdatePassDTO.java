package wpproject.project.dto;

public class AccountUpdatePassDTO {
    private String password;
    private String mail;

    public AccountUpdatePassDTO(String password, String mail) {
        this.password = password;
        this.mail = mail;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
