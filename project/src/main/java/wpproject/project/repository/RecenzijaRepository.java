package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Review;

public interface RecenzijaRepository extends JpaRepository<Review, Long> {
}
