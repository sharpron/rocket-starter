package pub.ron.admin.message.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.admin.message.dto.MessageHistoryQuery;
import pub.ron.admin.message.service.MessageHistoryService;
import pub.ron.admin.system.security.SubjectUtils;

/**
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
@Tag(name = "历史消息管理")
@RequiredArgsConstructor
public class MessageHistoryRest {

  private final MessageHistoryService messageHistoryService;


  @GetMapping
  @Operation(tags = "分页查询历史消息")
  @RequiresPermissions("message:query")
  public ResponseEntity<?> findByPage(Pageable pageable, MessageHistoryQuery query) {
    return ResponseEntity.ok(
        messageHistoryService.findByPage(pageable, query)
    );
  }

  @DeleteMapping
  @Operation(tags = "清空自己的历史消息")
  @RequiresPermissions("message:remove")
  public ResponseEntity<?> clear() {
    SubjectUtils.getCurrentUsername().ifPresent(
        messageHistoryService::deleteByUsername);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  @Operation(tags = "删除历史消息")
  @RequiresPermissions("message:remove")
  public ResponseEntity<?> remove(
      @PathVariable Long id) {
    messageHistoryService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
