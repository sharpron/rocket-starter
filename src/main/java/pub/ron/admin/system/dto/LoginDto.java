package pub.ron.admin.system.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

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

  private Boolean rememberMe;

  @NotBlank
  private String captchaKey;

  @NotBlank
  private String captcha;
}
