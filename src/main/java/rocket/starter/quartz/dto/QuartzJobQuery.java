package rocket.starter.quartz.dto;

import lombok.Data;
import rocket.starter.common.query.Where;

/**
 * quartz job query.
 *
 * @author herong 2021/2/9
 */
@Data
public class QuartzJobQuery {

  @Where(type = Where.Type.like)
  private String name;

  @Where(type = Where.Type.eq)
  private Boolean enabled;

  @Where(type = Where.Type.like)
  private String description;

}
