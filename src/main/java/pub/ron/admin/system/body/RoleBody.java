package pub.ron.admin.system.body;

import java.util.Set;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;

/**
 * 角色操作数据.
 *
 * @author ron 2022/8/8
 */
@Data
public class RoleBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  @Column(length = 20)
  @NotBlank(message = "角色名称不能为空")
  private String name;

  private String description;

  private Boolean disabled;

  private Set<Long> deptIds;

  private Set<Long> menuIds;
}
