package pub.ron.admin.system.repo;

import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Role;

/**
 * role repository.
 *
 * @author ron 2020/11/18
 */
@Repository
public interface RoleRepo extends BaseRepo<Role> {

  /**
   * 获取用户所管理的部门.
   *
   * @param userId 用户id
   * @return 部门id
   */
  @Query(value = "select dept_id from sys_role_dept d inner join sys_user_role r "
      + " on d.role_id=r.role_id where r.user_id=?1",
      nativeQuery = true)
  Set<Long> findManageDeptIds(Long userId);
}
