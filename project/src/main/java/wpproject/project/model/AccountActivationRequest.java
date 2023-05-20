package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;
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

    @Column
    protected Date requestDate;

    enum Status {WAITING, APPROVED, REJECTED}

    @Column
    protected Status status;

    @ManyToOne
    protected AccountAuthor author;
}
