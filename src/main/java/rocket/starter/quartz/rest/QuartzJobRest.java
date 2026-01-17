package rocket.starter.quartz.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;
import rocket.starter.quartz.body.QuartzJobBody;
import rocket.starter.quartz.dto.QuartzJobQuery;
import rocket.starter.quartz.dto.QuartzLogQuery;
import rocket.starter.quartz.service.QuartzJobService;
import rocket.starter.quartz.service.mapper.QuartzJobMapper;

/**
 * quartz job rest api.
 *
 * @author ron 2020/11/18
 */
@Slf4j
@RestController
@RequestMapping("/api/jobs")
@Tag(name = "定时任务管理")
@RequiredArgsConstructor
public class QuartzJobRest {

  private final QuartzJobService quartzJobService;
  private final QuartzJobMapper quartzJobMapper;

  @GetMapping
  @Operation(summary = "分页查询定时任务")
  @RequiresPermissions("job:query")
  public ResponseEntity<?> findByPage(Pageable pageable, QuartzJobQuery query) {
    return ResponseEntity.ok(quartzJobService.findByPage(pageable, query));
  }

  @GetMapping("/logs")
  @Operation(summary = "定时任务日志展示")
  @RequiresPermissions("job:query")
  public ResponseEntity<?> findByPage(Pageable pageable, QuartzLogQuery query) {
    return ResponseEntity.ok(quartzJobService.findLogsByPage(pageable, query));
  }

  /**
   * 修改定时任务状态.
   *
   * @param jobId job id
   * @return response
   */
  @PutMapping("/{id}/status")
  @Operation(summary = "修改定时任务状态")
  @RequiresPermissions("job:modify")
  public ResponseEntity<?> updateStatus(@PathVariable Long jobId) {
    quartzJobService.toggleEnabled(jobId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * 立即执行一次定时任务.
   *
   * @param jobId job id
   * @return response
   */
  @PostMapping("/{id}/executions")
  @Operation(summary = "立即执行一次定时任务")
  @RequiresPermissions("job:execute")
  public ResponseEntity<?> execute(@PathVariable Long jobId) {
    quartzJobService.execute(jobId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping
  @Operation(summary = "创建定时任务")
  @RequiresPermissions("job:create")
  public ResponseEntity<?> create(
      @RequestBody @Validated({Default.class, Create.class}) QuartzJobBody quartzJob) {
    quartzJobService.create(quartzJobMapper.mapQuartzJob(quartzJob));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  @Operation(summary = "修改定时任务")
  @RequiresPermissions("job:modify")
  public ResponseEntity<?> modify(
      @RequestBody @Validated({Default.class, Update.class}) QuartzJobBody quartzJob) {
    quartzJobService.update(quartzJobMapper.mapQuartzJob(quartzJob));
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(summary = "删除定时任务")
  @RequiresPermissions("job:remove")
  public ResponseEntity<?> remove(@RequestParam Set<Long> ids) {
    quartzJobService.deleteByIds(ids);
    return ResponseEntity.noContent().build();
  }
}
