package pub.ron.admin.system.security.realm;

import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.repo.RoleRepo;
import pub.ron.admin.system.repo.UserRepo;
import pub.ron.admin.system.security.principal.UserPrincipal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.ByteSource.Util;

/**
 * @author ron 2020/12/17
 */
@RequiredArgsConstructor
public class UserRealm extends AuthenticatingRealm {

  private final UserRepo userRepo;
  private final RoleRepo roleRepo;

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof UsernamePasswordToken;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {

    final User user = userRepo.findByUsername(
        (String) token.getPrincipal()
    ).orElseThrow(UnknownAccountException::new);

    if (user.getDisabled()) {
      throw new DisabledAccountException();
    }

    if (user.getLocked() == Boolean.TRUE) {
      throw new LockedAccountException();
    }

    final Set<Long> manageDeptIds = roleRepo.findManageDeptIds(user.getId());

    final UserPrincipal principal = UserPrincipal.builder()
        .id(user.getId())
        .username(user.getUsername())
        .deptId(user.getDept().getId())
        .deptPath(user.getDept().getPath())
        .deptIds(manageDeptIds)
        .build();
    final ByteSource salt = Util.bytes(user.getPasswordSalt());
    return new SimpleAuthenticationInfo(
        principal, user.getPassword(), salt, getName()
    );

  }
}
