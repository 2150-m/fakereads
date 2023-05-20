package wpproject.project.repository;

import wpproject.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnjigaRepository extends JpaRepository<Book, Long> {

}
