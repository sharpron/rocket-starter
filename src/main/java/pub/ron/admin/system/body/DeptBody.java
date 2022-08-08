package pub.ron.admin.system.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;

/**
 * 部门操作数据.
 *
 * @author ron 2022/8/8
 */
@Data
public class DeptBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  @NotBlank(message = "部门名称不能为空")
  private String name;

  private Integer orderNo;

  private Long parentId;

  private String path;
}
