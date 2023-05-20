package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.BookReview;

public interface ReviewRepository extends JpaRepository<BookReview, Long> {
}
