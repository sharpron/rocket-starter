package pub.ron.admin.system.body;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import pub.ron.admin.common.validator.Mobile;
import pub.ron.admin.common.validator.Password;

/**
 * @author ron 2020/11/18
 */
@Data
public class CreateUserBody {

  @NotBlank(message = "用户名称不能为空")
  private String username;

  @Password
  private String password;

  @Mobile
  private String mobile;

  @Email
  private String email;

  private Boolean disabled;

  private List<Long> roleIds;

  @NotNull
  private Long deptId;
}
