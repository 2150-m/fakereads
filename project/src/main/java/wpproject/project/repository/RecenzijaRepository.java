package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Recenzija;

public interface RecenzijaRepository extends JpaRepository<Recenzija, Long> {
}
