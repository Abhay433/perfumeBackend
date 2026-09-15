package quickcart_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"quickcart_backend", "module", "comman"})
@EnableJpaRepositories(basePackages = {"module"})
@EntityScan(basePackages = {"module"})
public class QuickcartBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickcartBackendApplication.class, args);
	}

}
