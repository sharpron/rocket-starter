package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuDto;
import pub.ron.admin.system.security.Principal;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.MenuService;
import pub.ron.admin.system.service.mapper.MenuMapper;

/**
 * menu rest api.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuRest {

  private final MenuService menuService;
  private final MenuMapper menuMapper;

  /**
   * query self menu.
   *
   * @return response
   */
  @GetMapping
  @Operation(tags = "查询所有菜单以树的格式")
  public ResponseEntity<?> getMenus() {
    final Principal userPrincipal = SubjectUtils.currentUser();
    final List<Menu> menusByUser = menuService.findMenusByUsername(userPrincipal.getUsername());
    List<MenuDto> result = new ArrayList<>();
    genTree(menusByUser, null, result);
    return ResponseEntity.ok(result);
  }

  private void genTree(List<Menu> inputs, Long parentId, List<MenuDto> outputs) {
    for (Menu input : inputs) {

      if (input.getParentId() == null && parentId == null
          || input.getParentId() != null && input.getParentId().equals(parentId)) {
        final MenuDto menuDto = menuMapper.mapDto(input);
        outputs.add(menuDto);
        menuDto.setChildren(new ArrayList<>());
        genTree(inputs, menuDto.getId(), menuDto.getChildren());
      }
    }
  }

  @PostMapping
  @Operation(tags = "创建菜单")
  @RequiresPermissions("menu:create")
  public ResponseEntity<?> create(@RequestBody @Valid Menu menu) {
    menuService.create(menu);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改菜单")
  @RequiresPermissions("menu:modify")
  public ResponseEntity<?> modify(@RequestBody @Valid Menu menu) {
    menuService.update(menu);
    return ResponseEntity.ok().build();
  }

  /**
   * 批量删除.
   *
   * @param ids ids
   * @return 响应
   */
  @DeleteMapping
  @Operation(tags = "删除菜单")
  @RequiresPermissions("menu:remove")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    for (Long id : ids) {
      menuService.deleteById(id);
    }
    return ResponseEntity.noContent().build();
  }
}
