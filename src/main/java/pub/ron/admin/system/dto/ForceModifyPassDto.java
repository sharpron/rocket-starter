package pub.ron.admin.system.dto;

import lombok.Data;
import pub.ron.admin.common.validator.Password;

/**
 * @author ron 2020/11/18
 */
@Data
public class ForceModifyPassDto {

  @Password
  private String username;

  @Password
  private String newPass;
}
