package pub.ron.admin.system.service;

import java.util.List;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.Menu;

/**
 * menu service.
 *
 * @author ron 2020/12/14
 */
public interface MenuService extends BaseService<Menu> {

  List<Menu> findMenusByUsername(String username);

}
