package pub.ron.admin.system.security;

import java.util.Set;
import lombok.Builder;
import lombok.Value;
import pub.ron.admin.system.domain.User;

/**
 * user principal.
 *
 * @author ron 2022/7/19
 */
@Builder
@Value
public class Principal {
  Long userId;
  String username;
  Long deptId;
  String deptPath;
  Set<String> perms;
  Set<Long> manageDeptIds;

  static PrincipalBuilder from(Principal principal) {
    return new PrincipalBuilder()
        .userId(principal.getUserId())
        .username(principal.getUsername())
        .deptId(principal.getUserId())
        .perms(principal.getPerms())
        .manageDeptIds(principal.getManageDeptIds());
  }

  public boolean isAdmin() {
    return username.equals(User.ADMIN);
  }
}
