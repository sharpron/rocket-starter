package rocket.starter.common.utils;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Captcha utils test.
 *
 * @author ron 2022/10/24
 */
public class CaptchaUtilsTest {

  @Test
  public void testGenerateText() {
    Set<String> set = new HashSet<>();
    int loopCount = 10 * 10000;
    for (int i = 0; i < loopCount; i++) {
      set.add(CaptchaUtils.generateText());
    }

    // 检查重复率
    double repeatRate = (loopCount - set.size()) / (double) loopCount;
    Assertions.assertTrue(repeatRate < 0.01, String.format("重复率为%.2f, 大于10%%", repeatRate));
  }

  @Test
  @DisplayName("20秒内生成100000张验证码")
  public void testGenerateImage() {
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(20), () -> {
      for (int i = 0; i < 10 * 10000; i++) {
        byte[] bytes = CaptchaUtils.generateImage(CaptchaUtils.generateText());
        Assertions.assertNotNull(bytes);
        Assertions.assertTrue(bytes.length > 0);
      }
    });
  }
}
