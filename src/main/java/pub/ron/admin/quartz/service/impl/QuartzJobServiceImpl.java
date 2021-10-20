package pub.ron.admin.quartz.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.AppException;
import pub.ron.admin.common.query.WhereBuilder;
import pub.ron.admin.quartz.core.QuartzJobManager;
import pub.ron.admin.quartz.domain.QuartzJob;
import pub.ron.admin.quartz.domain.QuartzLog;
import pub.ron.admin.quartz.dto.QuartzLogQuery;
import pub.ron.admin.quartz.repo.QuartzJobRepo;
import pub.ron.admin.quartz.repo.QuartzLogRepo;
import pub.ron.admin.quartz.service.QuartzJobService;

/**
 * quartz job service.
 *
 * @author herong 2021/2/9
 */
@Service
public class QuartzJobServiceImpl extends AbstractService<QuartzJob, QuartzJobRepo>
    implements QuartzJobService {

  private final QuartzLogRepo quartzLogRepo;
  private final QuartzJobManager quartzJobManager;

  /**
   * constructor.
   *
   * @param repository       quartz job repository
   * @param quartzLogRepo    quartzLogRepo
   * @param quartzJobManager quartzJobManager
   */
  public QuartzJobServiceImpl(
      QuartzJobRepo repository, QuartzLogRepo quartzLogRepo, QuartzJobManager quartzJobManager) {
    super(repository);
    this.quartzLogRepo = quartzLogRepo;
    this.quartzJobManager = quartzJobManager;
  }

  @Override
  public Page<QuartzLog> findLogsByPage(Pageable pageable, QuartzLogQuery query) {
    return quartzLogRepo.findAll(WhereBuilder.buildSpec(query), pageable);
  }

  @Override
  public void create(QuartzJob quartzJob) {
    super.create(quartzJob);
    quartzJobManager.addJob(quartzJob);
  }

  @Override
  public void update(QuartzJob quartzJob) {
    super.update(quartzJob);
    quartzJobManager.updateCron(quartzJob);
  }

  @Override
  public void pause(Long jobId) {
    if (repository.updateEnabled(jobId, false) > 0) {
      quartzJobManager.pause(jobId);
    } else {
      throw new AppException("暂停失败，任务状态异常");
    }
  }

  @Override
  public void resume(Long jobId) {
    if (repository.updateEnabled(jobId, true) > 0) {
      quartzJobManager.resume(jobId);
    } else {
      throw new AppException("恢复失败，任务状态异常");
    }
  }

  @Override
  public void deleteById(Long id) {
    super.deleteById(id);
    quartzJobManager.delete(id);
  }
}
