package wpproject.project.repository;

import wpproject.project.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository_Shelf extends JpaRepository<Shelf, Long> {
    Optional<Shelf> findByName(String name);
}