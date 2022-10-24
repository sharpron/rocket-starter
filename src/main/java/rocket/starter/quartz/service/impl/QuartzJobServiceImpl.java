package rocket.starter.quartz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.common.query.WhereBuilder;
import rocket.starter.quartz.core.QuartzJobManager;
import rocket.starter.quartz.domain.QuartzJob;
import rocket.starter.quartz.domain.QuartzLog;
import rocket.starter.quartz.dto.QuartzLogQuery;
import rocket.starter.quartz.repo.QuartzJobRepo;
import rocket.starter.quartz.repo.QuartzLogRepo;
import rocket.starter.quartz.service.QuartzJobService;

/**
 * quartz job service.
 *
 * @author herong 2021/2/9
 */
@Service
@RequiredArgsConstructor
public class QuartzJobServiceImpl extends AbstractService<QuartzJob>
    implements QuartzJobService {

  private final QuartzJobRepo repository;

  private final QuartzLogRepo quartzLogRepo;
  private final QuartzJobManager quartzJobManager;

  @Override
  public Page<QuartzLog> findLogsByPage(Pageable pageable, QuartzLogQuery query) {
    return quartzLogRepo.findAll(WhereBuilder.buildSpec(query), pageable);
  }

  @Override
  public void toggleEnabled(Long jobId) {
    QuartzJob quartzJob = repository.findById(jobId).orElseThrow();
    if (quartzJob.isEnabled()) {
      quartzJobManager.pause(jobId);
    } else {
      quartzJobManager.resume(jobId);
    }
    repository.updateEnabled(jobId, !quartzJob.isEnabled());
  }

  @Override
  protected BaseRepo<QuartzJob> getBaseRepo() {
    return repository;
  }

  @Override
  protected void afterCreate(QuartzJob quartzJob) {
    quartzJobManager.addJob(quartzJob);
  }

  @Override
  protected void afterUpdate(QuartzJob quartzJob) {
    quartzJobManager.updateCron(quartzJob);
  }


  @Override
  public void execute(Long jobId) {
    repository.findById(jobId).ifPresent(quartzJobManager::executeJobNow);
  }

  @Override
  protected void afterDelete(QuartzJob quartzJob) {
    quartzJobManager.delete(quartzJob.getId());
  }
}
