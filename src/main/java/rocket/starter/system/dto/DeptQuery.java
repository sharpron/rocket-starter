package rocket.starter.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import rocket.starter.common.query.Where;
import rocket.starter.common.query.Where.Type;

/**
 * dept query.
 *
 * @author ron 2020/11/19
 */
@Data
public class DeptQuery {

  @Where(type = Type.like)
  private String name;

  @Where
  private Boolean disabled;

  /**
   * 本地使用.
   */
  @Where(type = Type.right_like)
  @JsonIgnore
  private String path;
}
