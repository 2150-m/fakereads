package wpproject.project.repository;

import wpproject.project.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicaRepository extends JpaRepository<Shelf, Long> {

}