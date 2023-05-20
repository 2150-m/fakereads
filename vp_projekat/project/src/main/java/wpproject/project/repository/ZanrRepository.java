package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Genre;

public interface ZanrRepository extends JpaRepository<Genre, Long> {
}
