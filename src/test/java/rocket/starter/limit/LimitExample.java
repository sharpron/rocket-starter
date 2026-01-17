package rocket.starter.limit;

import org.springframework.stereotype.Component;

/**
 * 限流测试用例.
 *
 * @author ron 2021/10/20
 */
@Component
public class LimitExample {

  /**
   * 两秒内限制100次调用.
   */
  @Limit(periodMills = 2000, maxCount = 100)
  public void test100timesIn2s() {
  }


  /**
   * 两秒内限制1000次调用.
   */
  @Limit(periodMills = 2000, maxCount = 1000)
  public void test1000timesIn2s() {
  }

  /**
   * 两秒内限制100次调用.
   */
  @Limit(periodMills = 2000, maxCount = 100, type = Limit.Type.IP_ADDRESS)
  public void test100timesIn2sByIp() {
  }


  /**
   * 两秒内限制1000次调用.
   */
  @Limit(periodMills = 2000, maxCount = 1000, type = Limit.Type.IP_ADDRESS)
  public void test1000timesIn2sByIp() {
  }
}
