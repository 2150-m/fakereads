package WPPROJECT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import WPPROJECT.model.ShelfItem;

public interface ShelfItemRepository extends JpaRepository<ShelfItem, Long> {
}
