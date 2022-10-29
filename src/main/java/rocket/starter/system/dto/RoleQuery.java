package rocket.starter.system.dto;

import lombok.Data;
import rocket.starter.common.query.Where;
import rocket.starter.common.query.Where.Type;

/**
 * role query.
 *
 * @author ron 2020/11/22
 */
@Data
public class RoleQuery {

  @Where(type = Type.like)
  private String name;

  @Where
  private Boolean disabled;

  @Where(type = Type.like)
  private String description;

}
