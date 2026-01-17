package rocket.starter.system.dto;

import lombok.Data;
import rocket.starter.common.query.Where;
import rocket.starter.common.query.Where.Type;
import rocket.starter.system.domain.MenuType;

/**
 * role query.
 *
 * @author ron 2020/11/22
 */
@Data
public class MenuQuery {

  @Where(type = Type.like)
  private String title;

  @Where(type = Type.eq)
  private MenuType type;

  @Where(type = Type.like)
  private String path;
}
