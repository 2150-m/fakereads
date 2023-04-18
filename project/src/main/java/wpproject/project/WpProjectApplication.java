package wpproject.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import wpproject.project.repository.KorisnikRepository;

@SpringBootApplication
public abstract class WpProjectApplication implements CommandLineRunner {

	@Autowired
	private KorisnikRepository korisnikRepository;

	@Override
	public void run(String... args) {



	}

	public static void main(String[] args) {
		SpringApplication.run(WpProjectApplication.class, args);
	}

}
