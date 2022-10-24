package rocket.starter.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rocket.starter.common.utils.ExcelUtils;
import rocket.starter.common.utils.TreeUtils;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;
import rocket.starter.logging.Log;
import rocket.starter.system.body.MenuBody;
import rocket.starter.system.domain.Menu;
import rocket.starter.system.domain.MenuType;
import rocket.starter.system.dto.MenuDto;
import rocket.starter.system.dto.MenuQuery;
import rocket.starter.system.security.Principal;
import rocket.starter.system.security.SubjectUtils;
import rocket.starter.system.service.MenuService;
import rocket.starter.system.service.mapper.MenuMapper;

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
   * 查询自己的菜单.
   *
   * @return response
   */
  @GetMapping("me")
  @Operation(tags = "查询所有菜单以树的格式")
  public ResponseEntity<?> getSelfMenus() {
    final Principal userPrincipal = SubjectUtils.currentUser();
    final List<Menu> menusByUser = menuService
        .findMenusByUsername(userPrincipal.getUsername());
    List<Menu> menus = menusByUser.stream()
        .sorted(Comparator.comparingInt(Menu::getOrderNo))
        .collect(Collectors.toList());
    return ResponseEntity.ok(genTree(menus));
  }

  /**
   * 查询自己的菜单.
   *
   * @param menuQuery 菜单查询
   * @return response
   */
  @GetMapping
  @Operation(tags = "查询所有菜单以树的格式")
  @RequiresPermissions("menu:query")
  @Log("查询菜单")
  public ResponseEntity<?> findMenus(MenuQuery menuQuery) {
    Sort orderNo = Sort.by("orderNo");
    final List<Menu> menus = menuService.findAll(menuQuery, orderNo);
    return ResponseEntity.ok(genTree(menus));
  }

  /**
   * 下载excel格式的数据.
   *
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("menu:query")
  @Log("菜单导出")
  public ResponseEntity<Resource> getAsExcel(MenuQuery menuQuery) {
    Sort orderNo = Sort.by("orderNo");
    List<String[]> data = menuService.findAll(menuQuery, orderNo).stream()
        .map(e -> new String[] {
            e.getTitle(), e.getIcon(), String.valueOf(e.getOrderNo()), String.valueOf(e.getType()),
            e.getPath(), ExcelUtils.formatValue(e.getCacheable()),
            ExcelUtils.formatValue(e.isHide())})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(
        new String[] {"标题", "图标", "序号", "类型", "路径", "是否缓存", "是否隐藏"}, data);
    return ExcelUtils.buildResponse(resource);
  }

  /**
   * 查询菜单.字典内容.
   *
   * @return response
   */
  @GetMapping(params = "datatype=dict")
  @Operation(tags = "查询所有菜单以树的格式")
  public ResponseEntity<?> findMenusAsDict(MenuQuery menuQuery) {
    final List<Menu> menus = menuService.findAll(menuQuery);
    return ResponseEntity.ok(TreeUtils.buildTree(menuMapper.mapSmallDto(menus)));
  }

  private List<MenuDto> genTree(List<Menu> inputs) {
    List<MenuDto> menus = menuMapper.mapDto(inputs);
    return TreeUtils.buildTree(menus);
  }

  @PostMapping
  @Operation(tags = "创建菜单")
  @RequiresPermissions("menu:create")
  @Log("创建菜单")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) MenuBody menuBody) {
    menuService.create(beforeSave(menuMapper.mapMenu(menuBody)));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  private Menu beforeSave(Menu menu) {
    if (menu.getType() != MenuType.MENU) {
      menu.setCacheable(Boolean.FALSE);
    }
    if (menu.getType() == MenuType.BUTTON) {
      menu.setHide(true);
    }
    if (menu.getType() == MenuType.CATEGORY || menu.getType() == MenuType.BUTTON) {
      menu.setPath(null);
    }
    return menu;
  }

  @PutMapping
  @Operation(tags = "修改菜单")
  @RequiresPermissions("menu:modify")
  @Log("修改菜单")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) MenuBody menuBody) {
    menuService.update(beforeSave(menuMapper.mapMenu(menuBody)));
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
  @Log("删除菜单")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    menuService.deleteByIds(ids);
    return ResponseEntity.noContent().build();
  }
}
