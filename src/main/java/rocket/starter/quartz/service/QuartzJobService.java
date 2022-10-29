package rocket.starter.quartz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rocket.starter.common.BaseService;
import rocket.starter.quartz.domain.QuartzJob;
import rocket.starter.quartz.domain.QuartzLog;
import rocket.starter.quartz.dto.QuartzLogQuery;

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
