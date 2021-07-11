package pub.ron.admin.system.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author ron 2020/11/18
 */
@Data
public class LoginDto {

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String captcha;
}
