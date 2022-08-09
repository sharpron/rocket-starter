package pub.ron.admin.system.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
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
@RequiredArgsConstructor
public class MenuServiceImpl extends AbstractService<Menu> implements MenuService {

  private final MenuRepo menuRepo;

  @Override
  public List<Menu> findMenusByUsername(String username) {
    if (User.ADMIN.equals(username)) {
      return menuRepo.findAll();
    }
    return menuRepo.findMenusByUsername(username);
  }


  @Override
  protected BaseRepo<Menu> getBaseRepo() {
    return menuRepo;
  }
}
