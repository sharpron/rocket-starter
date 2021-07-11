package pub.ron.admin.system.body;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * role body.
 *
 * @author ron 2020/11/22
 */
@Data
public class RoleBody {

  @NotBlank private String name;

  @NotNull private Long deptId;

  private boolean disabled;

  @Size(max = 255)
  private String description;

  private List<Long> menus;

  private Set<Long> deptIds;
}
