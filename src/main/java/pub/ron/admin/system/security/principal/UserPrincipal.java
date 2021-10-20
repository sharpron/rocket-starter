package pub.ron.admin.system.security.principal;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import pub.ron.admin.system.domain.User;

/**
 * user principal.
 *
 * @author ron 2020/11/19
 */
@Builder
@Getter
public class UserPrincipal {

  private final Long id;

  private final String username;

  private final Long deptId;

  /**
   * 管辖的部门id.
   */
  private final Set<Long> deptIds;

  private final String deptPath;

  public boolean isAdmin() {
    return User.ADMIN.equals(getUsername());
  }
}
