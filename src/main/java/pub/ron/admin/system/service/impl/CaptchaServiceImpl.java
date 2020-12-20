package pub.ron.admin.system.service.impl;

import com.wf.captcha.ArithmeticCaptcha;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AppException;
import pub.ron.admin.system.service.CaptchaService;

/**
 * @author ron 2020/11/23
 */
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

  private static final String CAPTCHA_CACHE_KEY = "captcha:";

  private static final Duration EXPIRE = Duration.ofMinutes(5);

  private final StringRedisTemplate redisTemplate;

  @Override
  public Captcha genCaptcha() {
    ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 32, 2);
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    captcha.out(os);
    final String key = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set(
        CAPTCHA_CACHE_KEY + key, captcha.text(),
        EXPIRE
    );
    return new Captcha(
        key,
        os.toByteArray()
    );
  }

  @Override
  public void check(String key, String code) {
    final String oldCode = redisTemplate.opsForValue().get(
        CAPTCHA_CACHE_KEY + key);
    if (oldCode == null) {
      throw new AppException("验证码超时");
    }
    if (!oldCode.equals(code)) {
      throw new AppException("验证码错误");
    }
  }
}
