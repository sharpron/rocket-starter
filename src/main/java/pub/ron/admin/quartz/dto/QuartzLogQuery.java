package pub.ron.admin.quartz.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;

/**
 * quartz log query.
 *
 * @author herong 2021/2/9
 */
@Data
public class QuartzLogQuery {

  @Where(type = Where.Type.eq)
  private Long jobId;
}
