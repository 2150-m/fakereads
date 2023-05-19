package WPPROJECT.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class AccountActivationRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String email;

    @Column
    protected String phone;

    @Column
    protected String message;

    @Column
    protected Date date;

    @Column
    protected AccountActivationRequest_Status status;

    @ManyToOne
    protected AccountAuthor author;
}
