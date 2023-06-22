package wpproject.project.configuration;

import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import wpproject.project.model.Account;
import wpproject.project.model.AccountAuthor;
import wpproject.project.model.Account_Role;
import wpproject.project.model.Shelf;
import wpproject.project.repository.Repository_Account;
import wpproject.project.repository.Repository_AccountAuthor;
import wpproject.project.repository.Repository_Shelf;
import wpproject.project.service.Service_Account;
import wpproject.project.service.Service_Shelf;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DatabaseConfiguration {

    @Autowired
    private Repository_Account repositoryAccount;

    @Autowired
    private Repository_AccountAuthor repositoryAccountAuthor;

    @Autowired
    private Repository_Shelf repositoryShelf;

    public void CreateAccount(String firstName, String lastName, String username, String mailAddress, String password, LocalDate dateOfBirth, String profilePicture, String description, Account_Role accountRole) {

        if (accountRole == Account_Role.AUTHOR) {
            Account a = new Account(firstName, lastName, username, mailAddress, password, dateOfBirth, profilePicture, description, accountRole);
            AccountAuthor account = new AccountAuthor(a);
            //repositoryAccount.save(a);
            repositoryAccountAuthor.save(account);

            Shelf shelf_WantToRead = new Shelf("WantToRead", true);
            Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
            Shelf shelf_Read = new Shelf("Read", true);

            account.setShelves(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));
            repositoryAccountAuthor.save(account);

            repositoryShelf.save(shelf_WantToRead);
            repositoryShelf.save(shelf_CurrentlyReading);
            repositoryShelf.save(shelf_Read);
        }
        else {
            Account account = new Account(firstName, lastName, username, mailAddress, password, dateOfBirth, profilePicture, description, accountRole);
            repositoryAccount.save(account);

            Shelf shelf_WantToRead = new Shelf("WantToRead", true);
            Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
            Shelf shelf_Read = new Shelf("Read", true);

            account.setShelves(List.of(shelf_WantToRead, shelf_CurrentlyReading, shelf_Read));
            repositoryAccount.save(account);

            repositoryShelf.save(shelf_WantToRead);
            repositoryShelf.save(shelf_CurrentlyReading);
            repositoryShelf.save(shelf_Read);
        }
    }

    @Bean
    public boolean instantiate() {

        CreateAccount(
                "Big",
                "Boss",
                "snake",
                "snake@gmail.com",
                "123",
                LocalDate.now(),
                "/avatars/admin.jpg",
                "9 years ago...",
                Account_Role.ADMINISTRATOR
        );

        CreateAccount(
                "Scranton",
                "The Electric City",
                "scranton",
                "scranton@gmail.com",
                "123",
                LocalDate.now(),
                "/avatars/scranton.png",
                "i'm so scared",
                Account_Role.READER
        );

        CreateAccount(
                "Ivo",
                "Andric",
                "ivoandric",
                "ivoandric@gmail.com",
                "123",
                LocalDate.now(),
                "/avatars/ivo.jpg",
                "pisem knjige mnogo :)",
                Account_Role.AUTHOR
        );

        return true;
    }
}


