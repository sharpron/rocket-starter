package pub.ron.admin.quartz.core;

import com.mchange.lang.ThrowableUtils;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.QuartzJobBean;
import pub.ron.admin.quartz.domain.QuartzJob;
import pub.ron.admin.quartz.domain.QuartzLog;
import pub.ron.admin.quartz.repo.QuartzLogRepo;

/**
 * @author herong 2021/2/9
 */
@RequiredArgsConstructor
public class JobRunner extends QuartzJobBean implements ApplicationContextAware {

  static final String JOB_KEY = "QUARTZ_JOB";

  private final QuartzLogRepo quartzLogRepo;

  private ApplicationContext applicationContext;

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext) {

    final QuartzJob quartzJob = (QuartzJob) jobExecutionContext
        .getMergedJobDataMap().get(JOB_KEY);

    final QuartzLog quartzLog = new QuartzLog();
    quartzLog.setJobId(quartzJob.getId());

    final long start = System.currentTimeMillis();

    try {
      doJob(quartzJob);
      quartzLog.setSuccess(true);
    } catch (Throwable e) {
      quartzLog.setSuccess(false);
      quartzLog.setExceptionDetail(ThrowableUtils.extractStackTrace(e));
    } finally {
      quartzLog.setDuration(System.currentTimeMillis() - start);
      quartzLogRepo.save(quartzLog);
    }

  }

  private void doJob(QuartzJob quartzJob) {
    final Object bean = applicationContext.getBean(quartzJob.getRunnableBeanName());
    if (bean instanceof RunnableJob) {
      ((RunnableJob) bean).run(quartzJob.getParams());
    } else {
      throw new RuntimeException("JOB必须实现RunnableJob接口");
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
