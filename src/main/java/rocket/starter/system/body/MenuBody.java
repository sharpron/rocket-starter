package rocket.starter.system.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;
import rocket.starter.system.domain.MenuType;

/**
 * 菜单操作数据.
 *
 * @author ron 2022/8/8
 */
@Data
public class MenuBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  @NotBlank
  private String title;

  @NotNull
  private Integer orderNo;

  @NotNull
  private MenuType type;

  private String icon;

  private String path;

  private Boolean cacheable;

  private boolean hide;

  private String perm;

  private Long parentId;
}
