package rocket.starter.system.rest;

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
import rocket.starter.common.utils.ExcelUtils;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;
import rocket.starter.logging.Log;
import rocket.starter.system.body.DictItemBody;
import rocket.starter.system.domain.DictItem;
import rocket.starter.system.dto.DictItemQuery;
import rocket.starter.system.dto.DictItemSmallDto;
import rocket.starter.system.service.DictItemService;
import rocket.starter.system.service.mapper.DictItemMapper;

/**
 * 字典操作api.
 *
 * @author ron 2022/8/9
 */
@Slf4j
@RestController
@RequestMapping("/api/dictionary-items")
@Tag(name = "字典项管理")
@RequiredArgsConstructor
public class DictItemRest {

  private final DictItemService dictItemService;
  private final DictItemMapper dictItemMapper;


  /**
   * find dict item.
   *
   * @return self dept tree.
   */
  @GetMapping
  @Operation(tags = "查询字典项")
  @RequiresPermissions("dictionary:query")
  @Log("查询字典项")
  public ResponseEntity<?> findDictionaries(Pageable pageable, DictItemQuery dictItemQuery) {
    return ResponseEntity.ok(dictItemService.findByPage(pageable, dictItemQuery));
  }


  /**
   * 字典格式的数据.
   *
   * @param dictName the query criteria
   * @return 资源
   */
  @GetMapping(params = "datatype=dict")
  public ResponseEntity<List<DictItemSmallDto>> getDictItemsAsDict(
      @RequestParam String dictName) {
    List<DictItem> dictItems = dictItemService.findByDictName(dictName);
    return ResponseEntity.ok(dictItemMapper.mapSmalls(dictItems));
  }


  /**
   * 下载excel格式的数据.
   *
   * @param dictItemQuery the query criteria
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("dictionary:query")
  @Log("字典项导出")
  public ResponseEntity<Resource> getAsExcel(DictItemQuery dictItemQuery) {
    List<String[]> data = dictItemService.findAll(dictItemQuery).stream()
        .map(e -> new String[] {e.getName(), e.getValue(), String.valueOf(e.getOrderNo()),
            ExcelUtils.formatValue(e.getDisabled())})
        .collect(Collectors.toList());
    Resource resource = ExcelUtils.getExcelResource(new String[] {"名称", "值", "序号", "是否禁用"}, data);
    return ExcelUtils.buildResponse(resource);
  }

  @PostMapping
  @Operation(tags = "创建字典项")
  @RequiresPermissions("dictionary:create")
  @Log("创建字典项")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) DictItemBody dictBody) {
    dictItemService.create(dictItemMapper.mapDict(dictBody));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改字典项")
  @RequiresPermissions("dictionary:modify")
  @Log("修改字典项")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) DictItemBody dictBody) {
    dictItemService.update(dictItemMapper.mapDict(dictBody));
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(tags = "删除字典项")
  @RequiresPermissions("dictionary:remove")
  @Log("删除字典项")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    dictItemService.deleteByIds(ids);
    return ResponseEntity.noContent().build();
  }
}
