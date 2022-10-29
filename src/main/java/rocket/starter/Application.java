package rocket.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import rocket.starter.common.EnhancedSimpleJpaRepository;

/**
 * application entry point.
 *
 * @author ron 2020.10.22
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = EnhancedSimpleJpaRepository.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
