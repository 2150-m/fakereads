package wpproject.project.dto;

import wpproject.project.model.AccountActivationRequest;
import wpproject.project.model.AccountActivationRequest_Status;
import wpproject.project.model.AccountAuthor;

import java.time.LocalDate;

public class DTO_View_AccountActivationRequest {

    private Long id;
    private String mailAddress;
    private String phoneNumber;
    private String message;
    private LocalDate requestDate;
    private AccountActivationRequest_Status status;
    private AccountAuthor author;

    public DTO_View_AccountActivationRequest() {}

    public DTO_View_AccountActivationRequest(AccountActivationRequest accountActivationRequest) {
        this.id = accountActivationRequest.getId();
        this.mailAddress = accountActivationRequest.getMailAddress();
        this.phoneNumber = accountActivationRequest.getPhoneNumber();
        this.message = accountActivationRequest.getMessage();
        this.requestDate = accountActivationRequest.getRequestDate();
        this.status = accountActivationRequest.getStatus();
        this.author = accountActivationRequest.getAuthor();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMailAddress() { return mailAddress; }
    public void setMailAddress(String mailAddress) { this.mailAddress = mailAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    public AccountActivationRequest_Status getStatus() { return status; }
    public void setStatus(AccountActivationRequest_Status status) { this.status = status; }
    public AccountAuthor getAuthor() { return author; }
    public void setAuthor(AccountAuthor author) { this.author = author; }
}
