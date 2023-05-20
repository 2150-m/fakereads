package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.BookGenre;

public interface GenreRepository extends JpaRepository<BookGenre, Long> {
}
