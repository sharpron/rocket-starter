package rocket.starter.quartz.core;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import rocket.starter.quartz.domain.QuartzJob;
import rocket.starter.quartz.repo.QuartzJobRepo;

/**
 * quartz job runner.
 *
 * @author ron 2021-02-09
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuartzJobRunner implements ApplicationRunner {

  private final QuartzJobRepo quartzJobRepo;

  private final QuartzJobManager quartzJobManager;

  /**
   * 系统启动时需要重新启动job.
   *
   * @param applicationArguments applicationArguments
   */
  @Override
  public void run(ApplicationArguments applicationArguments) {
    List<QuartzJob> quartzJobs = quartzJobRepo.findByEnabledIs(true);
    if (quartzJobs.size() > 0) {
      quartzJobs.forEach(quartzJobManager::addJob);
      log.info("定时任务{}个已经启动完成..", quartzJobs.size());
    }
  }
}
