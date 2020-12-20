package pub.ron.admin.system.service;

import lombok.Value;

/**
 * @author ron 2020/11/23
 */
public interface CaptchaService {

  /**
   * @return 生成验证码
   */
  Captcha genCaptcha();

  /**
   * 检测验证码
   *
   * @param key  访问键
   * @param code 输入的验证码
   */
  void check(String key, String code);

  @Value
  class Captcha {

    String key;
    byte[] bytes;
  }
}
