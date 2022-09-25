package pub.ron.admin.system.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import pub.ron.admin.common.validator.Password;

/**
 * modify pass dto.
 *
 * @author ron 2020/11/18
 */
@Data
public class ModifyPassDto {

  @NotBlank
  @Password
  private String oldPass;

  @NotBlank
  @Password
  private String newPass;
}
