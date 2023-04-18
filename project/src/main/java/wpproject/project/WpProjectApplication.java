package wpproject.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import wpproject.project.model.Autor;
import wpproject.project.model.Korisnik;
import wpproject.project.repository.KorisnikRepository;
import wpproject.project.repository.AutorRepository;

@SpringBootApplication
public abstract class WpProjectApplication implements CommandLineRunner {

	@Autowired
	private KorisnikRepository korisnikRepository;
	@Autowired
	private AutorRepository autorRepository;

	@Override
	public void run(String... args) {
		//Korisnik korisnik = new Korisnik();
		//this.korisnikRepository.save(korisnik);

		Autor autor = new Autor();
		autor.setIme("Meme");
		autor.setNalogAktivan(true);
		this.autorRepository.save(autor);

		for (Korisnik k : this.korisnikRepository.findAll()) { System.out.println(k); }
	}

	public static void main(String[] args) {
		SpringApplication.run(WpProjectApplication.class, args);
	}

}
