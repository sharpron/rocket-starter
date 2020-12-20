package pub.ron.admin.system.repo;

import pub.ron.admin.system.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ron 2020/12/14
 */
@Repository
public interface MenuRepo extends JpaRepository<Menu, Long> {

  @Query("from Menu m left join RoleMenu rm on m.id = rm.menuId"
      + " left join UserRole ur on rm.roleId = ur.roleId"
      + " where ur.userId = ?1 order by m.orderNo")
  List<Menu> findMenusByUser(Long userId);

}
