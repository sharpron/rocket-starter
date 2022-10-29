package rocket.starter.system.repo;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Menu;

/**
 * menu repository.
 *
 * @author ron 2020/12/14
 */
@Repository
public interface MenuRepo extends BaseRepo<Menu> {

  /**
   * 查询用户所拥有的菜单.
   *
   * @param username username
   * @return 多个菜单
   */
  @Query(value = "select * from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id"
      + " left join sys_user_role ur on rm.role_id = ur.role_id"
      + " where ur.username = ?1 order by m.order_no",
      nativeQuery = true)
  List<Menu> findMenusByUsername(String username);
}
