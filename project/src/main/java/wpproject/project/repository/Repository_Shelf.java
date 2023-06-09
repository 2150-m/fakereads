package wpproject.project.repository;

import wpproject.project.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository_Shelf extends JpaRepository<Shelf, Long> {
    Shelf findByName(String name);
}