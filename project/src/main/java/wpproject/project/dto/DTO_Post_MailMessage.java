package wpproject.project.dto;

public class DTO_Post_MailMessage {

    private String message;

    public DTO_Post_MailMessage() {}

    public DTO_Post_MailMessage(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
