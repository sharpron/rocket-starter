package pub.ron.admin.system.body;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import pub.ron.admin.common.validator.Mobile;

/**
 * user base body.
 *
 * @author ron 2022/9/2
 */
@Data
public class UserBaseBody {


  @NotBlank
  private String nickname;

  @Mobile
  private String mobile;

  @Email
  private String email;

}
