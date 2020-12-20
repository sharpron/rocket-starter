package pub.ron.admin.system.dto;

import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;
import lombok.Data;

/**
 * @author ron 2020/11/22
 */
@Data
public class MenuQuery {


  @Where(type = Type.like)
  private String name;


}
