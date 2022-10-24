package rocket.starter.logging.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import rocket.starter.common.utils.ExcelUtils;
import rocket.starter.logging.Log;
import rocket.starter.logging.domain.Status;
import rocket.starter.logging.dto.LoggingQuery;
import rocket.starter.logging.service.LoggingService;
import rocket.starter.system.security.SubjectUtils;

/**
 * controller for logging.
 *
 * @author ron 2021/1/1
 */
@Slf4j
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LoggingController {

  private final LoggingService loggingService;

  @GetMapping("me")
  @Operation(tags = "分页查询本人日志")
  @RequiresAuthentication
  public ResponseEntity<?> findSelfByPage(Pageable pageable, LoggingQuery query) {
    query.setCreateBy(SubjectUtils.currentUser().getUsername());
    return ResponseEntity.ok(loggingService.findByPage(pageable, query));
  }

  @GetMapping
  @Operation(tags = "分页查询日志")
  @RequiresPermissions("log:query")
  @Log("查询日志")
  public ResponseEntity<?> findByPage(Pageable pageable, LoggingQuery query) {
    return ResponseEntity.ok(loggingService.findByPage(pageable, query));
  }

  /**
   * 下载excel格式的数据.
   *
   * @return 资源
   */
  @GetMapping("excels")
  @RequiresPermissions("log:query")
  @Log("日志导出")
  public ResponseEntity<StreamingResponseBody> getAsExcel(LoggingQuery query) {
    return ExcelUtils.buildResponse(
        new String[]{"操作描述", "参数", "间隔(ms)", "操作员", "时间", "客户端IP", "客户端位置", "浏览器", "异常信息"},
        lastId -> loggingService.findByPage(lastId, 1000, query).map(e -> new String[]{
            String.valueOf(e.getId()), e.getDescription(), e.getParams(),
            String.valueOf(e.getSpendTime()), e.getCreateBy(),
            e.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            e.getStatus() == Status.OK ? "正常" : "失败",
            e.getClientIp(), e.getClientRegion(), e.getUserAgent(), e.getException()}));
  }

  @DeleteMapping
  @Operation(tags = "清空日志")
  @RequiresPermissions("log:clear")
  @Log("清空日志")
  public ResponseEntity<?> clear(@RequestParam Status status) {
    loggingService.clear(status);
    return ResponseEntity.noContent().build();
  }

}
