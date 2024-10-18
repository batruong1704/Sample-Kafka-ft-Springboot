package site.authservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	NewTopic notification() {
		return new NewTopic(
				"notification-event",
				3,
				(short) 1
		);
	}

	@Bean
	NewTopic db() {
		return new NewTopic(
				"db-event",
				3,
				(short) 1
		);
	}

	@Bean
	NewTopic history() {
		return new NewTopic(
				"history-event",
				3,
				(short) 1
		);
	}
}
