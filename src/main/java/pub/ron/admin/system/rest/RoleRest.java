package pub.ron.admin.system.rest;

import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.dto.RoleQuery;
import pub.ron.admin.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRest {

  private final RoleService roleService;

  @GetMapping
  @Operation(tags = "分页查询角色")
  @RequiresPermissions("role:query")
  public ResponseEntity<?> findByPage(Pageable pageable, RoleQuery roleQuery) {
    return ResponseEntity.ok(
        roleService.findByPage(pageable, roleQuery)
    );
  }

  @GetMapping(params = "type=dict")
  @Operation(tags = "查询所有角色")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(
        roleService.findAll()
    );
  }

  @PostMapping
  @Operation(tags = "创建角色")
  @RequiresPermissions("role:create")
  public ResponseEntity<?> create(@RequestBody @Valid RoleBody roleBody) {
    roleService.create(roleBody);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .build();
  }

  @PutMapping("{id}")
  @Operation(tags = "修改角色")
  @RequiresPermissions("role:modify")
  public ResponseEntity<?> modify(
      @PathVariable Long id,
      @RequestBody @Valid RoleBody roleBody) {
    roleService.update(id, roleBody);
    return ResponseEntity.ok().build();
  }


  @DeleteMapping("{id}")
  @Operation(tags = "删除角色")
  @RequiresPermissions("role:remove")
  public ResponseEntity<?> remove(
      @PathVariable Long id) {
    roleService.remove(id);
    return ResponseEntity.ok().build();
  }


}
