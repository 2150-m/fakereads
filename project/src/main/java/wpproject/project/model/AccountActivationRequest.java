package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class AccountActivationRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String mailAddress;

    @Column
    protected String phoneNumber;

    @Column
    protected String message;

    @Temporal(TemporalType.DATE)
    @Column
    protected LocalDate requestDate;

    @Column
    protected AccountActivationRequest_Status status;

    @ManyToOne
    protected AccountAuthor author;

    public AccountActivationRequest() {}

    public AccountActivationRequest(String mailAddress, String phoneNumber, String message, LocalDate requestDate, AccountActivationRequest_Status status, AccountAuthor author) {
        this.mailAddress = mailAddress;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.requestDate = requestDate;
        this.status = status;
        this.author = author;
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
