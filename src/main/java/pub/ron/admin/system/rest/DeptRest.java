package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.Resource;
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
import pub.ron.admin.common.utils.TreeUtils;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;
import pub.ron.admin.logging.Log;
import pub.ron.admin.system.body.DeptBody;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.dto.DeptDto;
import pub.ron.admin.system.dto.DeptQuery;
import pub.ron.admin.system.security.Principal;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.DeptService;
import pub.ron.admin.system.service.mapper.DeptMapper;

/**
 * dept rest api.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/departments")
@Tag(name = "部门管理")
@RequiredArgsConstructor
public class DeptRest {

  private final DeptService deptService;
  private final DeptMapper deptMapper;

  /**
   * 以树的结构获取部门数据.
   *
   * @param deptQuery 部门查询
   * @return self dept tree.
   */
  @GetMapping
  @Operation(tags = "查询自己部门树")
  @RequiresPermissions("department:query")
  @Log("部门查询")
  public ResponseEntity<?> getSelfDepartments(DeptQuery deptQuery) {
    List<Dept> departments = deptService.findSelfDepartments(deptQuery);
    return ResponseEntity.ok(buildTree(departments));
  }

  /**
   * 下载excel格式的数据.
   *
   * @param deptQuery 部门查询
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("department:query")
  @Log("部门导出")
  public ResponseEntity<Resource> getAsExcel(DeptQuery deptQuery) {
    List<String[]> departments = deptService.findSelfDepartments(deptQuery)
        .stream().map(e -> new String[]{e.getName(), String.valueOf(e.getOrderNo())})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(new String[]{"部门名称", "序号"}, departments);
    return ExcelUtils.buildResponse(resource);
  }

  /**
   * 处理部门数据为树型.
   *
   * @param departments 部门数据
   * @return 数据
   */
  private List<DeptDto> buildTree(List<Dept> departments) {
    Principal principal = SubjectUtils.currentUser();
    Long deptId = principal.getDeptId();
    return TreeUtils.genTree(departments, (input, pid) -> {
      // 根节点判断
      if (pid == null && (input.getId().equals(deptId) || input.getParent() == null)) {
        return true;
      }
      Dept parent = input.getParent();
      return parent != null && Objects.equals(parent.getId(), pid);
    }, input -> {
      DeptDto deptDto = deptMapper.mapDto(input);
      deptDto.setChildren(new ArrayList<>());
      return new TreeUtils.Result<>(deptDto, input.getId(), deptDto.getChildren());
    });
  }

  /**
   * get dept tree.
   *
   * @param deptQuery 部门查询
   * @return self dept tree.
   */
  @GetMapping(params = "datatype=dict")
  @Operation(tags = "查询部门字典")
  public ResponseEntity<?> getSelfDepartmentsAsDict(DeptQuery deptQuery) {
    List<Dept> departments = deptService.findSelfDepartments(deptQuery);
    return ResponseEntity.ok(buildTree(departments));
  }

  @PostMapping
  @Operation(tags = "创建部门")
  @RequiresPermissions("department:create")
  @Log("创建部门")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) DeptBody deptBody) {
    deptService.create(deptMapper.mapDept(deptBody));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改部门")
  @RequiresPermissions("department:modify")
  @Log("修改部门")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) DeptBody deptBody) {
    deptService.update(deptMapper.mapDept(deptBody));
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(tags = "删除部门")
  @RequiresPermissions("department:remove")
  @Log("删除部门")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    deptService.deleteByIds(ids);
    return ResponseEntity.noContent().build();
  }
}
