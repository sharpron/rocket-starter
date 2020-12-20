package pub.ron.admin.system.security;

import pub.ron.admin.system.security.principal.UserPrincipal;

/**
 * @author ron 2020.12.19
 */
public interface TokenProvider {

  /**
   * 生成token
   *
   * @param principal  用户信息
   * @param rememberMe 是否启动记住我
   * @return token
   */
  String generateToken(UserPrincipal principal, boolean rememberMe);

  /**
   * 验证
   *
   * @param token token
   * @return 用户信息
   */
  UserPrincipal validate(String token);

  /**
   * 清除token
   *
   * @param token token
   */
  void clear(String token);
}
