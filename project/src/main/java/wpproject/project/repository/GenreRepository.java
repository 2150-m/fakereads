package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.BookGenre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<BookGenre, Long> {
    Optional<BookGenre> findByName(String name);
}
