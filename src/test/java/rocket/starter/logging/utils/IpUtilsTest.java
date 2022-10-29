package rocket.starter.logging.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ron 2022/10/25
 */
public class IpUtilsTest {

  @Test
  public void testIp2Region() {
    String region = IpUtils.ip2Region("171.212.113.114");
    Assertions.assertEquals("中国|0|四川省|成都市|电信", region);

    String region1 = IpUtils.ip2Region("14.204.0.255");
    Assertions.assertEquals("中国|0|云南省|昆明市|联通", region1);

    String region2 = IpUtils.ip2Region("218.70.26.255");
    Assertions.assertEquals("中国|0|重庆|重庆市|电信", region2);
  }
}
