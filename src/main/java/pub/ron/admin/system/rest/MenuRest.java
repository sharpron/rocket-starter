package pub.ron.admin.system.rest;

import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.dto.MenuQuery;
import pub.ron.admin.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuRest {

  private final MenuService menuService;

  @GetMapping
  @Operation(tags = "查询自己拥有的菜单")
  public ResponseEntity<?> findOwner(MenuQuery menuQuery) {
    return ResponseEntity.ok(
        menuService.findAsTree()
    );
  }

  @PostMapping
  @Operation(tags = "创建菜单")
  @RequiresPermissions("menu:create")
  public ResponseEntity<?> create(@RequestBody @Valid Menu menu) {
    menuService.create(menu);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .build();
  }

  @PutMapping("{id}")
  @Operation(tags = "修改菜单")
  @RequiresPermissions("menu:modify")
  public ResponseEntity<?> modify(
      @PathVariable Long id,
      @RequestBody @Valid Menu menu) {
    menu.setId(id);
    menuService.update(menu);
    return ResponseEntity.ok().build();
  }


  @DeleteMapping("{id}")
  @Operation(tags = "删除菜单")
  @RequiresPermissions("menu:remove")
  public ResponseEntity<?> remove(
      @PathVariable Long id) {
    menuService.removeById(id);
    return ResponseEntity.noContent().build();
  }


}
