package rocket.starter.system.dto;

import lombok.Data;
import rocket.starter.common.query.Where;

/**
 * 字典查询条件.
 *
 * @author ron 2022/8/8
 */
@Data
public class DictQuery {

  @Where(type = Where.Type.like)
  private String name;


  @Where(type = Where.Type.like)
  private String description;
}
