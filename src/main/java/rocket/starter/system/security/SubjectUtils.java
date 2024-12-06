package rocket.starter.system.security;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

/**
 * Subject Utils.
 *
 * @author ron 2022/8/8
 */
@Slf4j
public class SubjectUtils {

  /** Disable the constructor. */
  private SubjectUtils() {}

  /**
   * Get the login of the current user.
   *
   * @return the login of the current user.
   */
  public static Optional<String> getCurrentUsername() {
    return getCurrentUser().map(Principal::getUsername);
  }

  /**
   * 获取当前认证的用户名，如果没有则返回System.
   *
   * @return 认证的用户 or 系统
   */
  public static String currentUserNameOrSystem() {
    return SubjectUtils.getCurrentUsername().orElse("System");
  }

  public static Optional<Long> getCurrentDeptId() {
    return getCurrentUser().map(Principal::getDeptId);
  }

  public static Principal currentUser() {
    return getCurrentUser().orElseThrow(() -> new UnauthenticatedException("当前用户未登录"));
  }

  public static Optional<Principal> getCurrentUser() {
    final Object principal = org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
    return Optional.ofNullable((Principal) principal);
  }

  /**
   * update user principal.
   *
   * @param subject subject
   * @param principal principal
   */
  public static void updatePrincipal(Subject subject, Principal principal) {
    if (((Principal) subject.getPrincipal()).getUserId().equals(principal.getUserId())) {
      Session session = subject.getSession();
      PrincipalCollection pc =
          (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

      if (!pc.isEmpty()) {
        String realName = pc.getRealmNames().iterator().next();
        SimplePrincipalCollection collection = new SimplePrincipalCollection(principal, realName);
        session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, collection);
      }
    }
  }
}
