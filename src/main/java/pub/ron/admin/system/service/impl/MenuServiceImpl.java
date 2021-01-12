package pub.ron.admin.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import pub.ron.admin.system.repo.MenuRepo;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.service.MenuService;
import pub.ron.admin.system.service.mapper.MenuMapper;

/**
 * @author ron 2020/12/14
 */
@Service
//@CacheConfig(cacheNames = "menus")
public class MenuServiceImpl extends AbstractService<Menu, MenuRepo> implements MenuService {


  private final MenuMapper menuMapper;

  /**
   * 注入自己，走缓存
   */
  @Resource
  private MenuServiceImpl menuService;

  public MenuServiceImpl(MenuRepo repository,
      MenuMapper menuMapper) {
    super(repository);
    this.menuMapper = menuMapper;
  }


  @Override
  public List<Menu> findMenusByUser(Long userId) {
    return repository.findMenusByUser(userId);
  }

  @Override
  public List<MenuDto> findAsTree() {
    final UserPrincipal userPrincipal = SubjectUtils.currentUser();

    final List<Menu> menusByUser = userPrincipal.isAdmin() ?
        repository.findAll() : menuService.findMenusByUser(userPrincipal.getId());

    List<MenuDto> result = new ArrayList<>();
    genTree(menusByUser, null, result);
    return result;
  }

  private void genTree(List<Menu> inputs,
      Long parentId, List<MenuDto> outputs) {
    for (Menu input : inputs) {

      if (input.getParentId() == null && parentId == null ||
          input.getParentId() != null && input.getParentId().equals(parentId)) {
        final MenuDto menuDto = menuMapper.mapDto(input);
        outputs.add(menuDto);
        menuDto.setChildren(new ArrayList<>());
        genTree(inputs, menuDto.getId(), menuDto.getChildren());
      }

    }
  }
}
