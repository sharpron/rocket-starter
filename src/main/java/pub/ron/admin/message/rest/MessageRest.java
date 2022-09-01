package pub.ron.admin.message.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import pub.ron.admin.logging.Log;
import pub.ron.admin.message.body.ChatMessage;
import pub.ron.admin.message.body.PublishMessage;
import pub.ron.admin.message.dto.MessageHistoryQuery;
import pub.ron.admin.message.service.MessageHistoryService;
import pub.ron.admin.message.service.mapper.MessageHistoryMapper;

/**
 * controller for message history.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
@Tag(name = "消息管理")
@RequiredArgsConstructor
public class MessageRest {

  private final MessageHistoryService messageHistoryService;
  private final MessageHistoryMapper messageHistoryMapper;

  /**
   * 发布消息给所有人.
   *
   * @param message 消息
   * @return 结果
   */
  @PostMapping("/publishes")
  @Operation(tags = "发布消息")
  @RequiresPermissions("message:publish")
  @Log("发布消息")
  public ResponseEntity<?> publish(@RequestBody @Validated PublishMessage message) {
    messageHistoryService.publishMessage(message);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 发送消息给指定的人.
   *
   * @param message 消息
   * @return 结果
   */
  @PostMapping("/sends")
  @Operation(tags = "发送消息")
  @RequiresPermissions("message:send")
  @Log("发送消息")
  public ResponseEntity<?> publish(@RequestBody @Validated ChatMessage message) {
    messageHistoryService.sendMessage(message);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 修改状态为已读.
   *
   * @param ids 消息id.
   * @return 结果
   */
  @PutMapping("reads")
  @Operation(tags = "读取消息")
  @Log("读取消息")
  public ResponseEntity<?> updateRead(@RequestParam Set<Long> ids) {
    messageHistoryService.updateRead(ids);
    return ResponseEntity.ok().build();
  }

  /**
   * 全部已读.
   *
   * @return 结果
   */
  @PutMapping("reads-all")
  @Operation(tags = "全部已读")
  @Log("全部已读")
  public ResponseEntity<?> readAll() {
    messageHistoryService.readAll();
    return ResponseEntity.ok().build();
  }

  /**
   * 查询未读消息总数.
   *
   * @return 未读消息总数
   */
  @GetMapping("unread-counts")
  @Operation(tags = "查询未读消息总数")
  public ResponseEntity<?> getUnreadCount() {
    return ResponseEntity.ok(messageHistoryService.getUnreadCount());
  }

  @GetMapping(params = "datatype=small")
  @Operation(tags = "分页查询自己历史消息")
  public ResponseEntity<?> findSelfByPage(Pageable pageable) {
    return ResponseEntity.ok(
        messageHistoryService.findSelfByPage(pageable).map(messageHistoryMapper::mapSmall));
  }

  @GetMapping
  @Operation(tags = "分页查询历史消息")
  @RequiresPermissions("message:query")
  @Log("分页查询消息")
  public ResponseEntity<?> findByPage(Pageable pageable, MessageHistoryQuery query) {
    return ResponseEntity.ok(messageHistoryService.findByPage(pageable, query));
  }

  @DeleteMapping
  @Operation(tags = "删除历史消息")
  @RequiresPermissions("message:remove")
  @Log("删除历史消息")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    messageHistoryService.deleteByIds(ids);
    return ResponseEntity.noContent().build();
  }
}
