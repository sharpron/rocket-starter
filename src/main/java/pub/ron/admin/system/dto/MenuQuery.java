package pub.ron.admin.system.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;
import pub.ron.admin.system.domain.MenuType;

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
