package rocket.starter.logging.service;

import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rocket.starter.logging.domain.Logging;
import rocket.starter.logging.domain.Status;
import rocket.starter.logging.dto.LoggingQuery;

/**
 * @author ron 2022/10/27
 */
@SpringBootTest
@ActiveProfiles("test")
public class LoggingServiceTest {

  @Resource
  private LoggingService loggingService;

  @Test
  public void testAddLogForOperation() {
    Assertions.assertDoesNotThrow(() -> {
      Object o = loggingService.addLogForOperation(() -> 20, "TEST_LOG", "test_params");
      Assertions.assertEquals(20, o);
    });

    Assertions.assertThrows(Exception.class, () -> loggingService.addLogForOperation(() -> {
      throw new RuntimeException("test exception");
    }, "TEST_LOG", "test_params"));
  }

  @Test
  public void testClear() {
    testByStatus(Status.OK);
    testByStatus(Status.FAIL);
  }

  private void testByStatus(Status status) {
    loggingService.clear(status);
    LoggingQuery loggingQuery = new LoggingQuery();
    loggingQuery.setStatus(status);
    List<Logging> result = loggingService.findAll(loggingQuery);
    Assertions.assertTrue(result.isEmpty());
  }
}
