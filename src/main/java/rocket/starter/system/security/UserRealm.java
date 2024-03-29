package rocket.starter.system.security;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.ByteSource.Util;
import rocket.starter.system.domain.Menu;
import rocket.starter.system.domain.User;
import rocket.starter.system.service.MenuService;
import rocket.starter.system.service.RoleService;
import rocket.starter.system.service.UserService;

/**
 * user realm.
 *
 * @author ron 2022/7/18
 */
@RequiredArgsConstructor
public class UserRealm extends AuthorizingRealm {

  private final UserService userService;
  private final MenuService menuService;

  private final RoleService roleService;

  private final UserLocker userLocker;


  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof UsernamePasswordToken;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    if (principalCollection.fromRealm(getName()).isEmpty()) {
      return null;
    }
    Principal user = (Principal) principalCollection.getPrimaryPrincipal();

    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.addStringPermissions(user.getPerms());
    return authorizationInfo;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {

    String username = (String) token.getPrincipal();

    if (userLocker.isLocked(username)) {
      throw new LockedAccountException();
    }

    final User user = userService
        .findByUsername(username)
        .orElseThrow(UnknownAccountException::new);

    if (Boolean.TRUE.equals(user.getDisabled())) {
      throw new DisabledAccountException();
    }

    // 查询管理部门
    Set<Long> manageDeptIds = roleService.findManageDeptIds(user.getId());
    Set<String> permissions = menuService.findMenusByUserId(user.getId()).stream()
        .map(Menu::getPerm)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    Principal principal = Principal.builder()
        .username(user.getUsername())
        .nickname(user.getNickname())
        .mobile(user.getMobile())
        .email(user.getEmail())
        .userId(user.getId())
        .deptId(user.getDept().getId())
        .deptName(user.getDept().getName())
        .deptPath(user.getDept().getPath())
        .manageDeptIds(manageDeptIds)
        .perms(permissions)
        .passwordExpireAt(user.getPasswordExpireAt())
        .build();

    final ByteSource salt = Util.bytes(user.getPasswordSalt());
    return new SimpleAuthenticationInfo(principal, user.getPassword(), salt, getName());
  }


}
