package pub.ron.admin.system.service;

import java.util.List;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;

/**
 * @author ron 2020/12/14
 */
public interface MenuService extends BaseService<Menu> {

  List<Menu> findMenusByUser(Long userId);

  List<MenuDto> findAsTree();

}
