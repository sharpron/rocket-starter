package pub.ron.admin.system.security.provider;

import pub.ron.admin.system.security.principal.UserPrincipal;

/**
 * @author ron 2020.12.19
 */
public interface TokenProvider {

  /**
   * 生成token
   *
   * @param principal  用户信息
   * @return token
   */
  String generateToken(UserPrincipal principal);


  /**
   * 验证访问token
   *
   * @param token token
   * @return user principal if success
   * @throws TokenException 可能抛出
   */
  UserPrincipal validateToken(String token) throws TokenException;

  /**
   * 清除token
   * @param token token
   */
  void clear(String token);

}
