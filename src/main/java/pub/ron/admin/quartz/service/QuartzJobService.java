package pub.ron.admin.quartz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.quartz.domain.QuartzJob;
import pub.ron.admin.quartz.domain.QuartzLog;
import pub.ron.admin.quartz.dto.QuartzLogQuery;

/**
 * quartz job service.
 *
 * @author herong 2021/2/9
 */
@Service
public interface QuartzJobService extends BaseService<QuartzJob> {

  Page<QuartzLog> findLogsByPage(Pageable pageable, QuartzLogQuery query);

  void toggleEnabled(Long jobId);

  void execute(Long jobId);
}
