package pub.ron.admin.system.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;

/**
 * @author ron 2020/11/22
 */
@Data
public class RoleQuery {


  @Where(type = Type.like)
  private String name;

  @Where(root = "dept.id", type = Type.eq)
  private Long deptId;

}
