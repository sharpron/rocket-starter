package pub.ron.admin.property.rest;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import pub.ron.admin.common.AppException;
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
  public ResponseEntity<?> findByPage(Pageable pageable, PropertyCriteria criteria) {
    return ResponseEntity.ok(propertyService.findByPage(pageable, criteria));
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
        throw new AppException(HttpStatus.BAD_REQUEST, "值与类型不兼容");
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
  public ResponseEntity<?> create(@RequestBody @Valid Property property) {
    checkPropertyValue(property);
    propertyService.create(property);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * modify property.
   *
   * @param id       id
   * @param property property
   * @return response
   */
  @PutMapping("{id}")
  @Operation(tags = "修改属性")
  @RequiresPermissions("property:modify")
  public ResponseEntity<?> modify(@PathVariable Long id, @RequestBody @Valid Property property) {
    checkPropertyValue(property);
    // key属性不会被更新
    propertyService.update(property);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  @Operation(tags = "删除属性")
  @RequiresPermissions("property:remove")
  public ResponseEntity<?> remove(@PathVariable Long id) {
    propertyService.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
