package rocket.starter.system.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import rocket.starter.common.validator.Password;

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
