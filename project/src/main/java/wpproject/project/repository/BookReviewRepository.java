package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Account;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;

import java.util.Optional;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    //Optional<BookReview> findById(Long id);
    BookReview findByAccount(Account account);
}
