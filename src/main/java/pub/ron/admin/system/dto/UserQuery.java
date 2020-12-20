package pub.ron.admin.system.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;

/**
 * @author ron 2020/11/19
 */
@Data
public class UserQuery {

  @Where(type = Type.like)
  private String username;

  @Where(type = Type.like)
  private String mobile;

  @Where(type = Type.like)
  private String email;

  @Where(root = "dept.id", type = Type.eq)
  private Long deptId;

}
