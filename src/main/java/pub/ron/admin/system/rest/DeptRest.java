package pub.ron.admin.system.rest;

import pub.ron.admin.system.body.DeptBody;
import pub.ron.admin.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/depts")
@Tag(name = "部门管理")
@RequiredArgsConstructor
public class DeptRest {

  private final DeptService deptService;

  @GetMapping
  @Operation(tags = "获取自己部门树")
  public ResponseEntity<?> getSelfDepts() {
    return ResponseEntity.ok(
        deptService.findAsTree()
    );
  }

  @GetMapping(params = "type=full")
  @Operation(tags = "获取自己部门树")
  @RequiresPermissions("dept:self")
  public ResponseEntity<?> getSelfFullDepts() {
    return ResponseEntity.ok(
        deptService.findFullAsTree()
    );
  }

  @PostMapping
  @Operation(tags = "创建部门")
  @RequiresPermissions("dept:create")
  public ResponseEntity<?> create(@RequestBody @Valid DeptBody deptBody) {
    deptService.add(deptBody);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .build();
  }

  @PutMapping("{id}")
  @Operation(tags = "修改部门")
  @RequiresPermissions("dept:modify")
  public ResponseEntity<?> modify(
      @PathVariable Long id,
      @RequestBody @Valid DeptBody deptBody) {
    deptService.update(id, deptBody);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  @Operation(tags = "删除部门")
  @RequiresPermissions("dept:remove")
  public ResponseEntity<?> remove(
      @PathVariable Long id) {
    deptService.remove(id);
    return ResponseEntity.noContent().build();
  }

}
