package pub.ron.admin.system.security;

import pub.ron.admin.common.AppException;
import pub.ron.admin.system.security.principal.UserPrincipal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class SubjectUtils {


  /**
   * Disable the constructor
   */
  private SubjectUtils() {
  }

  /**
   * Get the login of the current user.
   *
   * @return the login of the current user.
   */
  public static Optional<String> getCurrentUsername() {
    return getCurrentUser().map(UserPrincipal::getUsername);
  }

  public static Optional<Long> getCurrentDeptId() {
    return getCurrentUser().map(UserPrincipal::getDeptId);
  }

  public static UserPrincipal currentUser() {
    return getCurrentUser().orElseThrow(() ->
        new AppException(HttpStatus.UNAUTHORIZED, "当前用户未登录"));
  }


  public static Optional<UserPrincipal> getCurrentUser() {
    final Object principal = org.apache.shiro.SecurityUtils.getSubject().getPrincipal();
    return Optional.ofNullable((UserPrincipal) principal);
  }
}
