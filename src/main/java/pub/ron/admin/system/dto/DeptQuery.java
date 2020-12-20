package pub.ron.admin.system.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;

/**
 * @author ron 2020/11/19
 */
@Data
public class DeptQuery {

  @Where(type = Type.like)
  private String name;

}
