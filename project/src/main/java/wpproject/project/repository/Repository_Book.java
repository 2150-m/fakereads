package wpproject.project.repository;

import wpproject.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository_Book extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
}
