package WPPROJECT.repository;

import WPPROJECT.model.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<BookReview, Long> {
}
