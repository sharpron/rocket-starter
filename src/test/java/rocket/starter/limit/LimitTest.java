package rocket.starter.limit;

import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rocket.starter.common.AppException;

/**
 * 限流测试.
 *
 * @author ron 2021/10/20
 */
@SpringBootTest
@ActiveProfiles("test")
public class LimitTest {

  @Resource
  private LimitExample limitExample;

  @Test
  @DisplayName("限流：通过方法名称测试")
  public void testLimitByMethodName() {
    // 两秒内执行100次正常
    for (int i = 0; i < 100; i++) {
      limitExample.test100timesIn2s();
    }

    // 2秒内超过100次调用会产生异常
    Assertions.assertThrows(AppException.class, () -> {
      for (int i = 0; i < 101; i++) {
        limitExample.test100timesIn2s();
      }
    }, "服务器过于繁忙，稍候再试");

    // 两秒内执行1000次正常
    for (int i = 0; i < 1000; i++) {
      limitExample.test1000timesIn2s();
    }

    // 2秒内超过1000次调用会产生异常
    Assertions.assertThrows(AppException.class, () -> {
      for (int i = 0; i < 1001; i++) {
        limitExample.test1000timesIn2s();
      }
    }, "服务器过于繁忙，稍候再试");
  }

  /**
   * 创建一个虚拟请求.
   *
   * @param ip ip地址
   */
  private void makeMockRequest(String ip) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr(ip);
    // 设置mock请求的ip地址
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  @Test
  @DisplayName("限流：通过IP地址测试")
  public void testLimitByIp() {
    makeMockRequest("10.1.1.11");
    // 两秒内执行100次正常
    for (int i = 0; i < 100; i++) {
      limitExample.test100timesIn2sByIp();
    }

    // 2秒内超过100次调用会产生异常
    Assertions.assertThrows(AppException.class, () -> {
      for (int i = 0; i < 101; i++) {
        limitExample.test100timesIn2sByIp();
      }
    }, "服务器过于繁忙，稍候再试");

    makeMockRequest("10.1.1.12");
    // 两秒内执行1000次正常
    for (int i = 0; i < 1000; i++) {
      limitExample.test1000timesIn2sByIp();
    }

    // 2秒内超过1000次调用会产生异常
    Assertions.assertThrows(AppException.class, () -> {
      for (int i = 0; i < 1001; i++) {
        limitExample.test1000timesIn2sByIp();
      }
    }, "服务器过于繁忙，稍候再试");
  }
}
