package pub.ron.admin.system.security.realm;

import java.util.Objects;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.security.JwtToken;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.security.provider.TokenException;
import pub.ron.admin.system.security.provider.TokenProvider;
import pub.ron.admin.system.service.MenuService;

/**
 * jwt realm.
 *
 * @author ron 2020/12/17
 */
public class JwtRealm extends AuthorizingRealm {

  private final TokenProvider tokenProvider;
  private final MenuService menuService;

  /**
   * constructor.
   *
   * @param tokenProvider tokenProvider
   * @param menuService menuService
   */
  public JwtRealm(TokenProvider tokenProvider, MenuService menuService) {
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
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    final UserPrincipal primaryPrincipal =
        (UserPrincipal) principalCollection.getPrimaryPrincipal();

    final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    menuService.findMenusByUser(primaryPrincipal.getId()).stream()
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
      final UserPrincipal principal = tokenProvider.validateToken(jwt);
      return new SimpleAuthenticationInfo(principal, null, getName());
    } catch (TokenException e) {
      switch (e.getResult()) {
        case EXPIRED:
          final String refreshToken = tokenProvider.generateToken(e.getUserPrincipal());
          throw new RefreshTokenException(refreshToken);
        case ILLEGAL:
          throw new AuthenticationException(e);
        default:
          throw new AssertionError();
      }
    }
  }
}
