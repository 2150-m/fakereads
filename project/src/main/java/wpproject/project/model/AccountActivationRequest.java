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
}
