package pub.ron.admin.quartz.service.mapper;

import org.springframework.stereotype.Component;
import pub.ron.admin.quartz.body.QuartzJobBody;
import pub.ron.admin.quartz.domain.QuartzJob;

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
