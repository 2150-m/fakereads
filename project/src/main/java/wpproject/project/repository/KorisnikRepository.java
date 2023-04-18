package wpproject.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wpproject.project.model.Korisnik;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

}
