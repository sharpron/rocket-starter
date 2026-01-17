package rocket.starter.quartz.dto;

import lombok.Data;
import rocket.starter.common.query.Where;

/**
 * quartz log query.
 *
 * @author herong 2021/2/9
 */
@Data
public class QuartzLogQuery {

  @Where(type = Where.Type.eq)
  private Long jobId;

  @Where(type = Where.Type.like)
  private Long exceptionDetail;
}
