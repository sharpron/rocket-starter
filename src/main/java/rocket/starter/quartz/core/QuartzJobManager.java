package rocket.starter.quartz.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;
import rocket.starter.common.AppException;
import rocket.starter.quartz.domain.QuartzJob;

/**
 * quartz job manager.
 *
 * @author herong 2021/2/9
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuartzJobManager {

  private static final String JOB_NAME = "JOB:";

  private final Scheduler scheduler;

  /**
   * 添加作业.
   *
   * @param quartzJob 作业
   */
  public void addJob(QuartzJob quartzJob) {
    try {
      final String jobIdentity = jobName(quartzJob.getId());
      JobDetail jobDetail = JobBuilder.newJob(JobRunner.class).withIdentity(jobIdentity).build();

      Trigger cronTrigger =
          TriggerBuilder.newTrigger()
              .withIdentity(jobIdentity)
              .startNow()
              .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
              .build();

      cronTrigger.getJobDataMap().put(JobRunner.JOB_KEY, quartzJob);
      // 执行定时任务
      scheduler.scheduleJob(jobDetail, cronTrigger);

      // 暂停任务
      if (!quartzJob.isEnabled()) {
        pause(quartzJob.getId());
      }
    } catch (Exception e) {
      log.error("创建定时任务失败", e);
      throw new AppException("创建定时任务失败");
    }
  }

  /**
   * 立即执行job.
   *
   * @param quartzJob /
   */
  public void executeJobNow(QuartzJob quartzJob) {
    try {
      TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      JobDataMap dataMap = new JobDataMap();
      dataMap.put(JobRunner.JOB_KEY, quartzJob);
      JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
      scheduler.triggerJob(jobKey, dataMap);
    } catch (Exception e) {
      log.error("定时任务执行失败", e);
      throw new AppException("定时任务执行失败");
    }
  }

  private String jobName(Long quartzJobId) {
    return JOB_NAME + quartzJobId;
  }

  /**
   * 暂停任务.
   *
   * @param jobId 任务id
   */
  public void pause(Long jobId) {
    final JobKey jobKey = JobKey.jobKey(jobName(jobId));
    try {
      scheduler.pauseJob(jobKey);
    } catch (SchedulerException e) {
      log.error("暂停定时任务失败!", e);
      throw new AppException("暂停定时任务失败!");
    }
  }

  /**
   * 删除任务.
   *
   * @param id id
   */
  public void delete(Long id) {
    final JobKey jobKey = JobKey.jobKey(jobName(id));
    try {
      scheduler.pauseJob(jobKey);
      scheduler.deleteJob(jobKey);
    } catch (SchedulerException e) {
      log.error("删除定时任务失败!", e);
      throw new AppException("删除定时任务失败!");
    }
  }

  /**
   * 恢复任务.
   *
   * @param jobId 任务id
   */
  public void resume(Long jobId) {
    final JobKey jobKey = JobKey.jobKey(jobName(jobId));
    try {
      scheduler.resumeJob(jobKey);
    } catch (SchedulerException e) {
      log.error("恢复定时任务失败!", e);
      throw new AppException("恢复定时任务失败!");
    }
  }

  /**
   * 更新cron 表达式.
   *
   * @param quartzJob job
   */
  public void updateCron(QuartzJob quartzJob) {
    TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
    try {
      CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      CronScheduleBuilder scheduleBuilder =
          CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
      trigger =
          trigger
              .getTriggerBuilder()
              .withIdentity(triggerKey)
              .withSchedule(scheduleBuilder)
              .build();
      trigger.getJobDataMap().put(JobRunner.JOB_KEY, quartzJob);

      scheduler.rescheduleJob(triggerKey, trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }
}
