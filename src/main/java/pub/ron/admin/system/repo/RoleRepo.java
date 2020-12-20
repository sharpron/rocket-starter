package pub.ron.admin.system.repo;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pub.ron.admin.system.domain.Role;

/**
 * @author ron 2020/11/18
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {


  @Query(value = "select dept_id from sys_role_dept d inner join sys_user_role r on d.role_id=r.role_id where r.user_id=?1", nativeQuery = true)
  Set<Long> findManageDeptIds(Long userId);

  void deleteByIdAndDept_IdIn(Long id, Collection<Long> deptIds);

}
