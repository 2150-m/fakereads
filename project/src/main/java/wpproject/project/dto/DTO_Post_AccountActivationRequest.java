package wpproject.project.dto;

public class DTO_Post_AccountActivationRequest {

    private String mailAddress;
    private String phoneNumber;
    private String message;

    public DTO_Post_AccountActivationRequest() {}

    public DTO_Post_AccountActivationRequest(String mailAddress, String phoneNumber, String message) {
        this.mailAddress = mailAddress;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getMailAddress() { return mailAddress; }
    public void setMailAddress(String mailAddress) { this.mailAddress = mailAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
