package wpproject.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: cleanup: useless imports from all files

// TODO: REST CLEANUP:
// TODO: -> remove all print functions (move all messages to front end) (ako npr obican user oce da accepta zahtev dobije forbidden ili tako nes)
// TODO: -> svaki rest controller bi trebao da vrati samo status (nema return (type podatka) vec uvek response neki) ili json

// TODO: cleanup: DTO -- ne sme user da vidi polja koja ne sme da vidi
// TODO: cleanup: DTO -- cleanup sve dto - treba da imaju svi konstruktore
// TODO: promeni sve urlove / use DeleteMapping kad brises elemente / promeni urlove u insomniji / test sve ponovo

@SpringBootApplication
public abstract class Application implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("App has started.");
	}
}
