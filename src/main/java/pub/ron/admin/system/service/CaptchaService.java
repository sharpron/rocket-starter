package pub.ron.admin.system.service;

import lombok.Value;

/**
 * captcha service.
 *
 * @author ron 2020/11/23
 */
public interface CaptchaService {

  /**
   * 生成验证码.
   *
   * @return 验证码
   */
  Captcha genCaptcha();

  /**
   * 检测验证码.
   *
   * @param key 访问键
   * @param code 输入的验证码
   */
  void check(String key, String code);

  /**
   * 获取验证码.
   *
   * @param key key
   * @return code
   */
  String getCode(String key);

  @Value
  class Captcha {

    String key;
    byte[] bytes;
  }
}
