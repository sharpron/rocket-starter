package pub.ron.admin.system.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import pub.ron.admin.common.query.Where;

/**
 * 字典查询条件.
 *
 * @author ron 2022/8/8
 */
@Data
public class DictItemDictQuery {

  @Where
  @NotNull
  private String dictName;
}
