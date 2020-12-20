package pub.ron.admin.system.security.realm;

import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.security.JwtToken;
import pub.ron.admin.system.security.TokenProvider;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.service.MenuService;
import io.jsonwebtoken.JwtException;
import java.util.Objects;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author ron 2020/12/17
 */
public class JwtRealm extends AuthorizingRealm {

  private final TokenProvider tokenProvider;
  private final MenuService menuService;

  public JwtRealm(TokenProvider tokenProvider,
      MenuService menuService) {
    this.tokenProvider = tokenProvider;
    this.menuService = menuService;
    // 不需要校验密码
    setCredentialsMatcher((a, b) -> true);
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JwtToken;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(
      PrincipalCollection principalCollection) {
    final UserPrincipal primaryPrincipal = (UserPrincipal)
        principalCollection.getPrimaryPrincipal();

    final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    menuService.findMenusByUser(
        primaryPrincipal.getId()).stream()
        .map(Menu::getPerm)
        .filter(Objects::nonNull)
        .forEach(info::addStringPermission);
    return info;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {

    final String jwt = (String) authenticationToken.getPrincipal();

    try {
      final UserPrincipal principal = tokenProvider.validate(jwt);
      return new SimpleAuthenticationInfo(principal, null, getName());
    } catch (JwtException e) {
      throw new AuthenticationException(e);
    }
  }
}
