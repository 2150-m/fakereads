package wpproject.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: cleanup: useless imports from all files
// TODO: cleanup: remove all print functions (move all messages to front end)
// TODO: cleanup: DTO -- ne sme user da vidi polja koja ne sme da vidi
// TODO: cleanup: DTO -- cleanup sve dto

// TODO: promeni sve urlove -- promeni urlove u insomniji - test sve ponovo
// TODO: use DeleteMapping kad brises elemente

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
