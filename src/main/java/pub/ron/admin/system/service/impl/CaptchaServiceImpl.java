package pub.ron.admin.system.service.impl;

import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AppException;
import pub.ron.admin.common.utils.CaptchaUtils;
import pub.ron.admin.system.service.CaptchaService;

/**
 * captcha service impl.
 *
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
    String captchaText = CaptchaUtils.generateText();
    byte[] bytes = CaptchaUtils.generateImage(captchaText);
    final String key = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set(CAPTCHA_CACHE_KEY + key, captchaText, EXPIRE);
    return new Captcha(key, bytes);
  }

  @Override
  public void check(String key, String code) {
    final String oldCode = getCode(key);
    if (oldCode == null) {
      throw new AppException("验证码超时");
    }
    if (!oldCode.equalsIgnoreCase(code)) {
      throw new AppException("验证码错误");
    }
  }

  @Override
  public String getCode(String key) {
    return redisTemplate.opsForValue().get(CAPTCHA_CACHE_KEY + key);
  }
}
