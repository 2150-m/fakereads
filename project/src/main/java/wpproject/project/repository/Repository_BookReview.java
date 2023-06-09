package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Account;
import wpproject.project.model.BookReview;

import java.util.List;

public interface Repository_BookReview extends JpaRepository<BookReview, Long> {
    List<BookReview> findByAccount(Account account);
}
