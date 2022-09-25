package pub.ron.admin.system.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import pub.ron.admin.common.validator.Password;

/**
 * login info.
 *
 * @author ron 2020/11/18
 */
@Data
public class LoginDto {

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @Password
  private String newPassword;

  @NotBlank
  private String captchaKey;

  @NotBlank
  private String captcha;
}
