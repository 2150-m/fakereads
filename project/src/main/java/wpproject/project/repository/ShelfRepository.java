package wpproject.project.repository;

import wpproject.project.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    Shelf findByName(String name);
}