package wpproject.project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wpproject.project.model.*;
import wpproject.project.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
public class DatabaseConfiguration {
    @Autowired
    private AccountActivationRequestRepository accountActivationRequestRepository;
    @Autowired
    private AccountRepository accountRepository;
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
        List<Shelf> defaultShelves = DefaultShelves();

        // USERS
        Account user001 = new Account("Milan", "Milanović", "milan1", "milan@milanovic.com", "milan123", LocalDate.of(1983, 4, 12), "pic1", "opis", Account_Role.READER);

        Account user002 = new Account("HBfhsfh", "Milanović", "fdfg", "milos@milanovic.com", "milan123", LocalDate.of(1983, 4, 12), "pic1", "opis", Account_Role.READER);

        // BOOKS
        Book book001 = new Book("BookestOfTheBooks", "picture0003.jpg", LocalDate.of(2003, 12, 9), "opis", 234, 7, "123455678");

        // BOOK GENRES
        BookGenre action = new BookGenre("Action");

        // REVIEWS
        BookReview review_book001_1 = new BookReview(8, "DOBRA KNJIGA TOSE NISAM NADO", LocalDate.of(2022, 7, 7), user001);

        // SHELF ITEMS
        ShelfItem shelfItem_book001 = new ShelfItem(book001);

        book001.getBookGenres().add(action);

        accountRepository.saveAll(List.of(user001, user002));
        genreRepository.saveAll(List.of(action));
        bookRepository.saveAll(List.of(book001));
        reviewRepository.saveAll(List.of(review_book001_1));
        shelfItemRepository.saveAll(List.of(shelfItem_book001));
        shelfRepository.saveAll(defaultShelves);

        shelfItem_book001.getBookReviews().add(review_book001_1);
        user001.getShelves().get(0).getShelfItems().add(shelfItem_book001);

        return true;
    }

    public static List<Shelf> DefaultShelves() {
        Shelf shelf_WantToRead = new Shelf("WantToRead", true);
        Shelf shelf_CurrentlyReading = new Shelf("CurrentlyReading", true);
        Shelf shelf_Read = new Shelf("Read", true);
        return List.of(shelf_Read, shelf_CurrentlyReading, shelf_WantToRead);
    }
//
//    public void SaveDefaultShelves(List<Shelf> list) {
//        shelfRepository.saveAll(list);
//    }
}
