package wpproject.project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wpproject.project.model.AccountUser;
import wpproject.project.model.Shelf;
import wpproject.project.repository.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Configuration
public class DatabaseConfiguration {
    @Autowired
    private AccountActivationRequestRepository accountActivationRequestRepository;
    @Autowired
    private AccountUserRepository accountUserRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ShelfItemRepository shelfItemRepository;
    @Autowired
    private ShelfRepository shelfRepository;

    @Bean
    public boolean instantiate() {
        // SHELVES
        Shelf shelf_ToRead = new Shelf("ToRead", true);
        Shelf shelf_Completed = new Shelf("Completed", true);
        Shelf shelf_Dropped = new Shelf("Dropped", true);

        shelfRepository.saveAll(List.of(
                shelf_ToRead, shelf_Completed, shelf_Dropped
        ));

        // USERS
        AccountUser user001 = new AccountUser("Milan", "Milanović", "milan1", "milan@milanovic.com", "milan123", new Date(1980, 10, 12), "pic1", "opis", AccountUser.AccountRole.READER);

        /*user001.getShelves().addAll(List.of(
                shelf_ToRead, shelf_Completed, shelf_Dropped
        ));*/

        accountUserRepository.saveAll(List.of(
                user001
        ));

        return true;
    }
}
