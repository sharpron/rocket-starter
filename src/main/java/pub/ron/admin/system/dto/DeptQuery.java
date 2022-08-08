package pub.ron.admin.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;

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
