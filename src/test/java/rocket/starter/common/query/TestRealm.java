package rocket.starter.common.query;

import java.util.Collections;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import rocket.starter.system.security.Principal;

/**
 * @author ron 2022/10/25
 */
class TestRealm extends AuthorizingRealm {

  TestRealm() {
    setCredentialsMatcher((e1, e2) -> true);
  }

  static final class EmptyToken implements AuthenticationToken {

    @Override
    public Object getPrincipal() {
      return null;
    }

    @Override
    public Object getCredentials() {
      return null;
    }
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof EmptyToken;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    return new SimpleAuthorizationInfo();
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    Principal principal = Principal.builder()
        .username("ron")
        .nickname("ron")
        .mobile("***********")
        .email("***********")
        .userId(1L)
        .deptId(1L)
        .deptName("***")
        .deptPath("***")
        .manageDeptIds(Collections.emptySet())
        .perms(Collections.emptySet())
        .build();

    return new SimpleAuthenticationInfo(principal, null, getName());
  }
}
