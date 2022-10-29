package rocket.starter.system.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;

/**
 * 字典操作数据.
 *
 * @author ron 2022/8/8
 */
@Data
public class DictBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  private String description;
}
