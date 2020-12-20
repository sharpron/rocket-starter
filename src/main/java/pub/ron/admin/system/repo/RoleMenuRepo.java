package pub.ron.admin.system.repo;

import pub.ron.admin.system.domain.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ron 2020/12/20
 */
@Repository
public interface RoleMenuRepo extends JpaRepository<RoleMenu, RoleMenu> {

  void deleteAllByRoleId(Long roleId);
}
