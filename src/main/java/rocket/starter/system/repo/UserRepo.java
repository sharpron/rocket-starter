package rocket.starter.system.repo;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.User;

/**
 * user repository.
 *
 * @author ron 2020/11/18
 */
@Repository
public interface UserRepo extends BaseRepo<User> {

  /**
   * 通过用户名查询用户.
   *
   * @param username 用户名
   * @return 用户
   */
  Optional<User> findByUsername(String username);

  /**
   * 更新用户密码.
   *
   * @param userId   用户ID
   * @param password 密码
   * @param salt     密码盐
   */
  @Query("update User set password=?2, passwordSalt=?3 where id=?1")
  @Modifying
  void updatePass(Long userId, String password, String salt);

  /**
   * 更新密码有效期.
   *
   * @param userId           用户ID
   * @param passwordExpireAt 密码过期时间
   */
  @Query("update User set passwordExpireAt=?2 where id=?1")
  @Modifying
  void updatePassLifetime(Long userId, LocalDateTime passwordExpireAt);
}
