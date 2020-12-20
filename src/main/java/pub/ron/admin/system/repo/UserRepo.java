package pub.ron.admin.system.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pub.ron.admin.system.domain.User;

/**
 * @author ron 2020/11/18
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findByUsername(String username);

  @Query("update User set mobile=:#{#user.mobile}, email=:#{#user.email}, disabled=:#{#user.disabled}, dept=:#{#user.dept} where id=:#{#user.id}")
  @Modifying
  void update(User user);

  @Query("update User set password=?2, passwordSalt=?3 where username=?1")
  @Modifying
  void updatePass(String username, String password, String salt);

  @Query(value = "insert into sys_user_role(user_id, role_id) values(?, ?)", nativeQuery = true)
  @Modifying
  void addRoleRelations(Long userId, Long roleId);

  @Query(value = "delete from sys_user_role where user_id=?", nativeQuery = true)
  @Modifying
  void clearRoles(Long userId);


}
