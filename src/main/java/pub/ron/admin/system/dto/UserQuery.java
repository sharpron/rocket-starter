package pub.ron.admin.system.dto;

import java.util.Set;
import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;

/**
 * user query.
 *
 * @author ron 2020/11/19
 */
@Data
public class UserQuery {

  @Where(type = Type.like)
  private String username;

  @Where(type = Type.like)
  private String nickname;

  @Where(type = Type.like)
  private String mobile;

  @Where(type = Type.like)
  private String email;

  @Where(root = "dept.id", type = Type.in)
  private Set<Long> deptIds;
}
