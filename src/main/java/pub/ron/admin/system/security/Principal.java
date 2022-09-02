package pub.ron.admin.system.security;

import java.io.Serializable;
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
public class Principal implements java.security.Principal, Serializable {

  Long userId;
  String username;
  Long deptId;
  String deptPath;
  Set<String> perms;
  Set<Long> manageDeptIds;
  
  public boolean isAdmin() {
    return username.equals(User.ADMIN);
  }

  @Override
  public String getName() {
    return username;
  }
}
