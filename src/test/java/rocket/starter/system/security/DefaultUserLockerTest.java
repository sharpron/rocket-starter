package rocket.starter.system.security;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author ron 2022/10/27
 */
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@ActiveProfiles("test")
public class DefaultUserLockerTest {

  private static final String TEST_USER = "test_user";

  @Resource
  private DefaultUserLocker userLocker;


  private void testLocked() {
    Assertions.assertEquals(3, userLocker.getMaxTryTimes());
    Assertions.assertEquals(Duration.ofSeconds(10), userLocker.getLockDuration());
    // 清除锁定标记
    userLocker.unlock(TEST_USER);
    int maxTryTimes = userLocker.getMaxTryTimes();
    for (int i = 0; i < maxTryTimes - 1; i++) {
      Assertions.assertFalse(userLocker.tryLocked(TEST_USER));
    }
    // 最后一次尝试锁定就会锁定
    Assertions.assertTrue(userLocker.tryLocked(TEST_USER));
  }

  @Test
  public void testTryLocked() throws InterruptedException {
    testLocked();

    Assertions.assertTrue(userLocker.isLocked(TEST_USER));
    TimeUnit.SECONDS.sleep(9);
    Assertions.assertTrue(userLocker.isLocked(TEST_USER));
    TimeUnit.SECONDS.sleep(1);
    // 十秒过后自动解锁
    Assertions.assertFalse(userLocker.isLocked(TEST_USER));
  }

  @Test
  public void testTryLockedWithMultiTryAfterLocked() throws InterruptedException {
    testLocked();

    TimeUnit.SECONDS.sleep(9);
    Assertions.assertTrue(userLocker.isLocked(TEST_USER));
    // 已经锁定的情况下再次尝试锁定会延长锁定时间
    userLocker.tryLocked(TEST_USER);
    TimeUnit.SECONDS.sleep(1);
    // 验证上次时间到了不会解锁
    Assertions.assertTrue(userLocker.isLocked(TEST_USER));
    TimeUnit.SECONDS.sleep(10);
    // 十秒过后自动解锁
    Assertions.assertFalse(userLocker.isLocked(TEST_USER));
  }
}
