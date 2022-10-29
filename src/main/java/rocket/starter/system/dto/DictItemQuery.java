package rocket.starter.system.dto;

import lombok.Data;
import rocket.starter.common.query.Where;

/**
 * 字典查询条件.
 *
 * @author ron 2022/8/8
 */
@Data
public class DictItemQuery {

  @Where(type = Where.Type.like)
  private String name;

  @Where
  private Boolean disabled;

  @Where
  private Long dictId;
}
