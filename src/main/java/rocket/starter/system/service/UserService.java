package rocket.starter.system.service;

import java.time.LocalDateTime;
import java.util.Optional;
import rocket.starter.common.BaseService;
import rocket.starter.system.body.UserBaseBody;
import rocket.starter.system.domain.User;
import rocket.starter.system.dto.ModifyPassDto;

/**
 * user service.
 *
 * @author ron 2020/11/18
 */
public interface UserService extends BaseService<User> {

  /**
   * 通过用户名查询用户.
   *
   * @param username username
   * @return 用户
   */
  Optional<User> findByUsername(String username);

  /**
   * 修改密码，需要传原密码.
   *
   * @param modifyPassDto modifyPassDto
   */
  void modifyPass(ModifyPassDto modifyPassDto);

  /**
   * 强制修改用户密码.
   *
   * @param userId   userId
   * @param password password
   */
  void forceModifyPass(Long userId, String password);

  /**
   * 修改用户基本信息.
   *
   * @param userBaseBody userBaseBody
   */
  void modifyUserBase(UserBaseBody userBaseBody);

  /**
   * 检查密码是否过期.
   *
   * @param passwordExpireAt 密码过期时间
   * @return true or false
   */
  boolean isPassExpired(LocalDateTime passwordExpireAt);
}
