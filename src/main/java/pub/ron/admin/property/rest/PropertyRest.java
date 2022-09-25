package pub.ron.admin.property.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import pub.ron.admin.common.AppException;
import pub.ron.admin.common.utils.ExcelUtils;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;
import pub.ron.admin.logging.Log;
import pub.ron.admin.property.domain.Property;
import pub.ron.admin.property.domain.ValueType;
import pub.ron.admin.property.dto.PropertyCriteria;
import pub.ron.admin.property.service.PropertyService;

/**
 * 属性操作接口.
 *
 * @author ron 2020/11/19
 */
@Slf4j
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyRest {

  private final PropertyService propertyService;

  /**
   * 分页查询属性.
   *
   * @param pageable pageable
   * @param criteria criteria
   * @return response
   */
  @GetMapping
  @Operation(tags = "分页查询属性")
  @RequiresPermissions("property:query")
  @Log("查询属性")
  public ResponseEntity<?> findByPage(Pageable pageable, PropertyCriteria criteria) {
    return ResponseEntity.ok(propertyService.findByPage(pageable, criteria));
  }

  /**
   * 下载excel格式的数据.
   *
   * @param criteria criteria
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("property:query")
  @Log("导出属性")
  public ResponseEntity<Resource> getAsExcel(PropertyCriteria criteria) {
    List<String[]> data = propertyService.findAll(criteria)
        .stream().map(e -> new String[] {e.getReferenceKey(), e.getValue(),
            e.getValueType().getDesc(), e.getDescription()})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(new String[] {"引用键", "配置值", "类型", "描述"}, data);
    return ExcelUtils.buildResponse(resource);
  }

  /**
   * 检查属性值是否符合规范.
   *
   * @param property 属性
   */
  private void checkPropertyValue(Property property) {
    if (property.getValueType() == ValueType.NUMBER
        && StringUtils.isNotBlank(property.getValue())) {
      boolean isNumber = property.getValue().matches("\\d+");
      if (!isNumber) {
        throw new AppException("值与类型不兼容");
      }
    }
  }

  /**
   * 创建属性.
   *
   * @param property 属性.
   * @return response
   */
  @PostMapping
  @Operation(tags = "创建属性")
  @RequiresPermissions("property:create")
  @Log("创建属性")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) Property property) {
    checkPropertyValue(property);
    propertyService.create(property);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * modify property.
   *
   * @param property property
   * @return response
   */
  @PutMapping
  @Operation(tags = "修改属性")
  @RequiresPermissions("property:modify")
  @Log("修改属性")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) Property property) {
    checkPropertyValue(property);
    // key属性不会被更新
    propertyService.update(property);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(tags = "删除属性")
  @RequiresPermissions("property:remove")
  @Log("删除属性")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    propertyService.deleteByIds(ids);
    return ResponseEntity.ok().build();
  }

}
