package rocket.starter.logging;

import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rocket.starter.common.AppException;

/**
 * 日志测试.
 *
 * @author ron 2021/10/20
 */
@SpringBootTest
@ActiveProfiles("test")
public class LogTest {

  @Resource
  private LogExample logExample;

  /**
   * 测试@Log注解.
   */
  @Test
  public void testLogAnnotation() {
    makeMockRequest();
    logExample.causeVisitLog("参数内容");

    Assertions.assertThrows(AppException.class, () -> logExample.causeErrorLog());
  }

  /**
   * 创建一个虚拟请求.
   */
  private void makeMockRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("10.1.1.11");
    request.addHeader(HttpHeaders.USER_AGENT, "Mock Http Request/1.0");
    // 设置mock请求的ip地址
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }
}
