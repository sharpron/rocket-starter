package rocket.starter.logging.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rocket.starter.logging.domain.Status;

/**
 * @author ron 2022/10/25
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL;",
    "spring.datasource.driver-class-name=org.h2.Driver"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoggingRepositoryTest {

  @SuppressWarnings("unused")
  @Autowired
  private LoggingRepository loggingRepository;

  @Test
  public void test() {
    loggingRepository.deleteByStatus(Status.OK);
  }
}
