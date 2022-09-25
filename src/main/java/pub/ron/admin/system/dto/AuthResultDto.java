package pub.ron.admin.system.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import pub.ron.admin.system.security.Principal;

/**
 * 认证结果.
 *
 * @author ron 2022/9/25
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResultDto {

  /**
   * 认证状态.
   */
  public enum Status {
    /**
     * 成功.
     */
    OK,
    /**
     * 密码过期.
     */
    PASSWORD_EXPIRE,
    /**
     * 修改过期密码完成.
     */
    MODIFY_EXPIRE_SUCCESS,
  }

  Status status;

  Principal principal;


  /**
   * 认证成功.
   *
   * @param principal principal
   * @return 结果
   */
  public static AuthResultDto ok(Principal principal) {
    return new AuthResultDto(Status.OK, principal);
  }

  /**
   * 其它状态.
   *
   * @param status 其它状态
   * @return 结果
   */
  public static AuthResultDto other(Status status) {
    if (status == Status.OK) {
      throw new IllegalArgumentException();
    }
    return new AuthResultDto(status, null);
  }
}
