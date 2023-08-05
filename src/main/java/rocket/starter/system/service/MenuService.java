package rocket.starter.system.service;

import java.util.List;
import rocket.starter.common.BaseService;
import rocket.starter.system.domain.Menu;

/**
 * menu service.
 *
 * @author ron 2020/12/14
 */
public interface MenuService extends BaseService<Menu> {

  List<Menu> findMenusByUserId(Long userId);

}
