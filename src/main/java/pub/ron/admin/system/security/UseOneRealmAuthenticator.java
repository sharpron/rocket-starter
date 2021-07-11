package pub.ron.admin.system.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * 重写 {@link ModularRealmAuthenticator} 使得异常处理正常 多realm的情况下，realm只使用匹配的一个处理
 * realm适用多种条件登录，多种方式登录应该只使用一个realm，然后再通过条件选择.
 *
 * @author ron 2019.01.17
 */
public class UseOneRealmAuthenticator extends ModularRealmAuthenticator {

  @Override
  protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    assertRealmsConfigured();
    for (Realm realm : getRealms()) {
      if (realm.supports(authenticationToken)) {
        return doSingleRealmAuthentication(realm, authenticationToken);
      }
    }
    return null;
  }
}
