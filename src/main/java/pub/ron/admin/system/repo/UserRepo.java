package pub.ron.admin.system.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.User;

/**
 * @author ron 2020/11/18
 */
@Repository
public interface UserRepo extends BaseRepo<User> {

  /**
   * 通过用户名查询用户
   *
   * @param username 用户名
   * @return 用户
   */
  Optional<User> findByUsername(String username);

  /**
   * 更新用户密码
   *
   * @param username 用户名
   * @param password 密码
   * @param salt     密码盐
   */
  @Query("update User set password=?2, passwordSalt=?3 where username=?1")
  @Modifying
  void updatePass(String username, String password, String salt);

}
