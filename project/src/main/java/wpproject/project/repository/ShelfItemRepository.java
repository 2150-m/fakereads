package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Book;
import wpproject.project.model.ShelfItem;

import java.util.Optional;

public interface ShelfItemRepository extends JpaRepository<ShelfItem, Long> {
    ShelfItem findByBook(Book book);
}
