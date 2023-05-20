package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.ShelfItem;

public interface StavkaPoliceRepository extends JpaRepository<ShelfItem, Long> {
}
