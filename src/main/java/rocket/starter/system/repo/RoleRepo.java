package rocket.starter.system.repo;

import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Role;

/**
 * role repository.
 *
 * @author ron 2020/11/18
 */
@Repository
public interface RoleRepo extends BaseRepo<Role> {

  /**
   * 通过用户id查询所有角色id.
   *
   * @param userId 用户id
   * @return 角色id
   */
  @Query(value = "select distinct role_id from sys_user_role where user_id=?1", nativeQuery = true)
  Set<Long> findRolesByUserId(Long userId);

  /**
   * 获取用户所管理的部门.
   *
   * @param roleIds 角色id
   * @return 部门id
   */
  @Query(
      value =
          "select path from sys_dept sd inner join sys_role_dept srd on sd.id=srd.dept_id "
              + "where srd.role_id in (:roleIds)",
      nativeQuery = true)
  Set<String> findManageDeptPaths(@Param("roleIds") Set<Long> roleIds);
}
