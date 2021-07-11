package pub.ron.admin.system.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * dept body.
 *
 * @author ron 2020/11/20
 */
@Data
public class DeptBody {

  @NotBlank(message = "部门名称不能为空")
  private String name;

  @NotNull(message = "部门序号不能为空")
  private Integer orderNo;

  @NotNull(message = "父部门不能为空")
  private Long parentId;
}
