package rocket.starter.system.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import rocket.starter.common.validator.Password;

/**
 * force modify pass.
 *
 * @author ron 2020/11/18
 */
@Data
public class ForceModifyPassDto {

  @NotNull
  private Long userId;

  @NotBlank
  @Password
  private String newPass;
}
