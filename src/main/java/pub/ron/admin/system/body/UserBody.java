package pub.ron.admin.system.body;

import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Mobile;
import pub.ron.admin.common.validator.Password;
import pub.ron.admin.common.validator.Update;

/**
 * create user body.
 *
 * @author ron 2020/11/18
 */
@Data
public class UserBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  @NotBlank(message = "用户名称不能为空", groups = Create.class)
  @Null(groups = Update.class)
  private String username;

  @NotBlank
  private String nickname;

  @Password
  @Null(groups = Update.class)
  private String password;

  @NotBlank
  @Mobile
  private String mobile;

  @NotBlank
  @Email
  private String email;

  private Boolean disabled;

  private Set<Long> roleIds;

  @NotNull
  private Long deptId;
}
