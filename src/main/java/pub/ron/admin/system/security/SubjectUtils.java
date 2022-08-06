package pub.ron.admin.system.security;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.http.HttpStatus;
import pub.ron.admin.common.AppException;

@Slf4j
public class SubjectUtils {

  /**
   * Disable the constructor.
   */
  private SubjectUtils() {
  }

  /**
   * Get the login of the current user.
   *
   * @return the login of the current user.
   */
  public static Optional<String> getCurrentUsername() {
    return getCurrentUser().map(Principal::getUsername);
  }

  public static Optional<Long> getCurrentDeptId() {
    return getCurrentUser().map(Principal::getDeptId);
  }

  public static Principal currentUser() {
    return getCurrentUser().orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "当前用户未登录"));
  }

  public static Optional<Principal> getCurrentUser() {
    final Object principal = org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
    return Optional.ofNullable((Principal) principal);
  }

  /**
   * update user principal.
   *
   * @param subject   subject
   * @param principal principal
   */
  public static void updatePrincipal(Subject subject, Principal principal) {
//    String realName = subject.getPrincipals().getRealmNames().iterator().next();
//    SimplePrincipalCollection simplePrincipalCollection =
//        new SimplePrincipalCollection(principal, realName);
//    subject.runAs(simplePrincipalCollection);
//    225333000012
//    829407EAF933410FAB11947472589CB7

    //225333000013
    SessionsSecurityManager securityManager =
        (DefaultSecurityManager) SecurityUtils.getSecurityManager();
    DefaultSessionManager d= (DefaultSessionManager) securityManager.getSessionManager();
    SessionDAO sessionDAO = d.getSessionDAO();
//    sessionDAO.getActiveSessions()

    Session session = subject.getSession();
    session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, principal);
  }
}
