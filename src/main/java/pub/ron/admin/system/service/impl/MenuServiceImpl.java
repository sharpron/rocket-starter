package pub.ron.admin.system.service.impl;

import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import pub.ron.admin.system.repo.MenuRepo;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.MenuService;
import pub.ron.admin.system.service.mapper.MenuMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ron 2020/12/14
 */
@Service
@RequiredArgsConstructor
//@CacheConfig(cacheNames = "menus")
public class MenuServiceImpl implements MenuService {

  private final MenuRepo menuRepo;

  private final MenuMapper menuMapper;

  /**
   * 注入自己，走缓存
   */
  @Resource
  private MenuServiceImpl menuService;


  @Override
  public List<Menu> findMenusByUser(Long userId) {
    return menuRepo.findMenusByUser(userId);
  }

  @Override
  public List<MenuDto> findAsTree() {
    final List<Menu> menusByUser = menuService.findMenusByUser(
        SubjectUtils.currentUser().getId()
    );

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

  @Override
//  @CacheEvict
  public void create(Menu menu) {
    menuRepo.save(menu);
  }

  @Override
//  @CacheEvict
  public void update(Menu menu) {
    menuRepo.save(menu);
  }


  @Override
//  @CacheEvict
  public void removeById(Long id) {
    menuRepo.deleteById(id);
  }
}
