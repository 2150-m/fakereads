package wpproject.project.dto;

public class DTO_Post_AccountUpdatePass {
    private String password;
    private String mail;

    public DTO_Post_AccountUpdatePass() {}

    public DTO_Post_AccountUpdatePass(String password, String mail) {
        this.password = password;
        this.mail = mail;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
