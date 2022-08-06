package pub.ron.admin.system.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.repo.MenuRepo;
import pub.ron.admin.system.service.MenuService;

/**
 * menu service impl.
 *
 * @author ron 2020/12/14
 */
@Service
public class MenuServiceImpl extends AbstractService<Menu, MenuRepo> implements MenuService {


  public MenuServiceImpl(MenuRepo repository) {
    super(repository);
  }

  @Override
  public List<Menu> findMenusByUsername(String username) {
    if (User.ADMIN.equals(username)) {
      return repository.findAll();
    }
    return repository.findMenusByUsername(username);
  }


}
