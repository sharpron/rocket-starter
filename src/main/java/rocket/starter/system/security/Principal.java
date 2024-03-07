package rocket.starter.system.security;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Value;
import rocket.starter.system.domain.User;

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
  String nickname;
  String mobile;
  String email;
  Long deptId;
  String deptName;
  String deptPath;
  Set<String> perms;
  Set<Long> manageDeptIds;
  LocalDateTime passwordExpireAt;


  public boolean isAdmin() {
    return username.equals(User.ADMIN);
  }

  @Override
  public String getName() {
    return username;
  }
}
