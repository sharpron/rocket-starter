package rocket.starter.system.body;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import rocket.starter.common.validator.Mobile;

/**
 * user base body.
 *
 * @author ron 2022/9/2
 */
@Data
public class UserBaseBody {


  @NotBlank
  private String nickname;

  @NotBlank
  @Mobile
  private String mobile;

  @NotBlank
  @Email
  private String email;

}
