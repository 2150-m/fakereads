package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Book;
import wpproject.project.model.ShelfItem;

public interface Repository_ShelfItem extends JpaRepository<ShelfItem, Long> {
    ShelfItem findByBook(Book book);
}
