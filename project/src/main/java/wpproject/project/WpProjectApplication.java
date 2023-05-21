package wpproject.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class WpProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(WpProjectApplication.class, args);
	}
}
