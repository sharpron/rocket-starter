package rocket.starter.quartz.service.mapper;

import org.springframework.stereotype.Component;
import rocket.starter.quartz.body.QuartzJobBody;
import rocket.starter.quartz.domain.QuartzJob;

/**
 * quartz job mapper.
 *
 * @author ron
 */
@Component
public class QuartzJobMapper {


  /**
   * 转换数据.
   *
   * @param quartzJobBody quartzJobBody
   * @return quartzJob
   */
  public QuartzJob mapQuartzJob(QuartzJobBody quartzJobBody) {
    QuartzJob quartzJob = new QuartzJob();
    quartzJob.setName(quartzJobBody.getName());
    quartzJob.setRunnableBeanName(quartzJobBody.getRunnableBeanName());
    quartzJob.setParams(quartzJobBody.getParams());
    quartzJob.setCronExpression(quartzJobBody.getCronExpression());
    quartzJob.setEnabled(quartzJobBody.isEnabled());
    quartzJob.setDescription(quartzJobBody.getDescription());
    quartzJob.setId(quartzJobBody.getId());
    return quartzJob;
  }
}
