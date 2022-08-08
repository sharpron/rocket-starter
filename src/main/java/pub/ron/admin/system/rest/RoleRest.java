package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
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
import pub.ron.admin.common.utils.ExcelUtils;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;
import pub.ron.admin.logging.Log;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.dto.RoleQuery;
import pub.ron.admin.system.service.RoleService;
import pub.ron.admin.system.service.mapper.RoleMapper;

/**
 * role rest api.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRest {

  private final RoleService roleService;
  private final RoleMapper roleMapper;

  /**
   * paged role query.
   *
   * @param pageable  pageable
   * @param roleQuery roleQuery
   * @return response
   */
  @GetMapping
  @Operation(tags = "分页查询角色")
  @RequiresPermissions("role:query")
  @Log("查询角色")
  public ResponseEntity<?> findByPage(Pageable pageable, RoleQuery roleQuery) {
    return ResponseEntity.ok(roleService.findByPage(pageable, roleQuery).map(roleMapper::mapDto));
  }

  /**
   * query all roles.
   *
   * @return all roles
   */
  @GetMapping(params = "type=dict")
  @Operation(tags = "查询所有角色")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(
        roleService.findAll(null).stream().map(roleMapper::mapDto).collect(Collectors.toList()));
  }

  /**
   * 下载excel格式的数据.
   *
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("role:query")
  @Log("角色导出")
  public ResponseEntity<Resource> getAsExcel(RoleQuery roleQuery) {
    List<String[]> data = roleService.findAll(roleQuery).stream()
        .map(e -> new String[] {
            e.getName(), e.getDescription(), ExcelUtils.formatValue(e.getDisabled())})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(
        new String[] {"角色名称", "描述", "是否禁用"}, data);
    return ExcelUtils.buildResponse(resource);
  }

  @PostMapping
  @Operation(tags = "创建角色")
  @RequiresPermissions("role:create")
  @Log("创建角色")
  public ResponseEntity<?> create(@RequestBody @Validated(Create.class) RoleBody roleBody) {
    roleService.create(roleMapper.mapRole(roleBody));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * modify role.
   *
   * @param roleBody roleBody
   * @return response
   */
  @PutMapping
  @Operation(tags = "修改角色")
  @RequiresPermissions("role:modify")
  @Log("修改角色")
  public ResponseEntity<?> modify(@RequestBody @Validated(Update.class) RoleBody roleBody) {
    roleService.update(roleMapper.mapRole(roleBody));
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(tags = "删除角色")
  @RequiresPermissions("role:remove")
  @Log("删除角色")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    roleService.deleteByIds(ids);
    return ResponseEntity.ok().build();
  }
}
