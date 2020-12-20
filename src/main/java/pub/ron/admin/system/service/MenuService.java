package pub.ron.admin.system.service;

import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import java.util.List;

/**
 * @author ron 2020/12/14
 */
public interface MenuService {

  List<Menu> findMenusByUser(Long userId);

  List<MenuDto> findAsTree();

  void create(Menu menu);

  void update(Menu menu);

  void removeById(Long id);

}
