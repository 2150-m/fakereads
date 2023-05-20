package wpproject.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import wpproject.project.model.AccountAuthor;
import wpproject.project.model.AccountUser;
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

		AccountAuthor accountAuthor = new AccountAuthor();
		accountAuthor.setId(3L);
		accountAuthor.setFirstName("Milos");
		accountAuthor.setLastName("Mihailovic");
		accountAuthor.setAccountActivated(true);
		this.autorRepository.save(accountAuthor);

		for (AccountUser k : this.korisnikRepository.findAll()) { System.out.println(k); }
	}

	public static void main(String[] args) {
		SpringApplication.run(WpProjectApplication.class, args);
	}

}
