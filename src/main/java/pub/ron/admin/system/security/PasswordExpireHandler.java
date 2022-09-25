package pub.ron.admin.system.security;

/**
 * 密码过期处理器.
 *
 * @author ron 2022/9/25
 */
public interface PasswordExpireHandler {

  /**
   * 检查用户是否过期.
   *
   * @param principal 用户
   * @return true or false
   */
  boolean isExpired(Object principal);


}
