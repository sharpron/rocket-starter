package pub.ron.admin.logging.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.admin.logging.dto.LoggingQuery;
import pub.ron.admin.logging.service.LoggingService;

/**
 * @author ron 2021/1/1
 */
@Slf4j
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LoggingController {

  private final LoggingService loggingService;


  @GetMapping
  @Operation(tags = "分页查询日志")
  @RequiresPermissions("log:query")
  public ResponseEntity<?> findByPage(Pageable pageable, LoggingQuery query) {
    return ResponseEntity.ok(
        loggingService.findByPage(pageable, query)
    );
  }
}
