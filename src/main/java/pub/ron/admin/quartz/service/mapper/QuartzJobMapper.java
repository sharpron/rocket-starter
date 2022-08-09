package pub.ron.admin.quartz.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.quartz.body.QuartzJobBody;
import pub.ron.admin.quartz.domain.QuartzJob;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuartzJobMapper {

  QuartzJob mapQuartzJob(QuartzJobBody quartzJobBody);
}
