package pub.ron.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author ron 2020.10.22
 */
@SpringBootApplication
@EnableJpaAuditing
public class SwtGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(SwtGatewayApplication.class, args);
  }

}
