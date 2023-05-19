package WPPROJECT;

import WPPROJECT.repository.AccountAuthorRepository;
import WPPROJECT.repository.AccountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import WPPROJECT.model.AccountAuthor;
import WPPROJECT.model.AccountUser;

@SpringBootApplication
public abstract class Application implements CommandLineRunner {

	@Autowired
	private AccountUserRepository accountUserRepository;
	@Autowired
	private AccountAuthorRepository accountAuthorRepository;

	@Override
	public void run(String... args) {

		// AccountAuthor author = new AccountAuthor();
		// author.setId(3L);
		// author.setFirstname("Milos");
		// author.setLastname("Mihailovic");
		// author.setActivated(true);
		// this.accountAuthorRepository.save(author);

		for (AccountUser k : this.accountUserRepository.findAll()) { System.out.println(k); }
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
