package pub.ron.admin.quartz.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pub.ron.admin.quartz.domain.QuartzJob;
import pub.ron.admin.quartz.dto.QuartzJobQuery;
import pub.ron.admin.quartz.dto.QuartzLogQuery;
import pub.ron.admin.quartz.dto.StatusType;
import pub.ron.admin.quartz.service.QuartzJobService;

/**
 * quartz job rest api.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "定时任务")
@RequiredArgsConstructor
public class QuartzJobRest {

  private final QuartzJobService quartzJobService;

  @GetMapping
  @Operation(tags = "分页查询定时任务")
  @RequiresPermissions("task:query")
  public ResponseEntity<?> findByPage(Pageable pageable, QuartzJobQuery query) {
    return ResponseEntity.ok(quartzJobService.findByPage(pageable, query));
  }

  @GetMapping("/logs")
  @Operation(tags = "定时任务日志展示")
  @RequiresPermissions("task:query")
  public ResponseEntity<?> findByPage(Pageable pageable, QuartzLogQuery query) {
    return ResponseEntity.ok(quartzJobService.findLogsByPage(pageable, query));
  }

  /**
   * 修改定时任务状态.
   *
   * @param jobId      job id
   * @param statusType statusType
   * @return response
   */
  @PutMapping("/{id}/status")
  @Operation(tags = "修改定时任务状态")
  @RequiresPermissions("task:modify")
  public ResponseEntity<?> updateStatus(@PathVariable Long jobId, StatusType statusType) {
    if (statusType == StatusType.PAUSE) {
      quartzJobService.pause(jobId);
    } else {
      quartzJobService.resume(jobId);
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping
  @Operation(tags = "创建定时任务")
  @RequiresPermissions("task:create")
  public ResponseEntity<?> create(@RequestBody @Valid QuartzJob quartzJob) {
    quartzJobService.create(quartzJob);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(tags = "修改定时任务")
  @RequiresPermissions("task:modify")
  public ResponseEntity<?> modify(@RequestBody @Valid QuartzJob quartzJob) {
    quartzJobService.update(quartzJob);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  @Operation(tags = "删除定时任务")
  @RequiresPermissions("task:remove")
  public ResponseEntity<?> remove(@PathVariable Long id) {
    quartzJobService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
