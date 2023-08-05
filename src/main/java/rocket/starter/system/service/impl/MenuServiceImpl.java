package rocket.starter.system.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Menu;
import rocket.starter.system.repo.MenuRepo;
import rocket.starter.system.service.MenuService;

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
  public List<Menu> findMenusByUserId(Long userId) {
    return menuRepo.findMenusByUserId(userId);
  }


  @Override
  protected BaseRepo<Menu> getBaseRepo() {
    return menuRepo;
  }
}
