package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.groups.Default;
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
import pub.ron.admin.system.body.DictBody;
import pub.ron.admin.system.dto.DictQuery;
import pub.ron.admin.system.service.DictService;
import pub.ron.admin.system.service.mapper.DictMapper;

/**
 * 字典操作api.
 *
 * @author ron 2022/8/8
 */
@Slf4j
@RestController
@RequestMapping("/api/dictionaries")
@Tag(name = "字典管理")
@RequiredArgsConstructor
public class DictRest {

  private final DictService dictService;
  private final DictMapper dictMapper;


  /**
   * find dict data.
   *
   * @return self dept tree.
   */
  @GetMapping
  @Operation(tags = "查询字典")
  @RequiresPermissions("dictionary:query")
  @Log("查询字典")
  public ResponseEntity<?> findDictionaries(Pageable pageable, DictQuery dictQuery) {
    return ResponseEntity.ok(dictService.findByPage(pageable, dictQuery));
  }


  /**
   * 下载excel格式的数据.
   *
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("dictionary:query")
  @Log("字典导出")
  public ResponseEntity<Resource> getAsExcel(DictQuery dictQuery) {
    List<String[]> data = dictService.findAll(dictQuery).stream()
        .map(e -> new String[]{e.getName(), e.getDescription()})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(new String[]{"字典名称", "描述"}, data);
    return ExcelUtils.buildResponse(resource);
  }

  @PostMapping
  @Operation(tags = "创建字典")
  @RequiresPermissions("dictionary:create")
  @Log("创建字典")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) DictBody dictBody) {
    dictService.create(dictMapper.mapDict(dictBody));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改字典")
  @RequiresPermissions("dictionary:modify")
  @Log("修改字典")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) DictBody dictBody) {
    dictService.update(dictMapper.mapDict(dictBody));
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(tags = "删除字典")
  @RequiresPermissions("dictionary:remove")
  @Log("删除字典")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    dictService.deleteByIds(ids);
    return ResponseEntity.noContent().build();
  }
}
