package rocket.starter.system.dto;

import lombok.Value;

/**
 * captcha.
 *
 * @author ron 2021/9/14
 */
@Value
public class CaptchaDto {
  String key;
  String base64Data;
}
