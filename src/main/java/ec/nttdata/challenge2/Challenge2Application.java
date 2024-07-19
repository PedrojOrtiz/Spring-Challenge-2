package ec.nttdata.challenge2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Challenge2Application {

	public static void main(String[] args) {
		SpringApplication.run(Challenge2Application.class, args);
	}

}
