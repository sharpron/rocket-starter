package pub.ron.admin.system.body;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import pub.ron.admin.common.validator.Create;
import pub.ron.admin.common.validator.Update;

/**
 * dict item body.
 *
 * @author ron 2022/8/9
 */
@Data
public class DictItemBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  /**
   * 引用名称.
   */
  private String name;

  /**
   * 引用的值.
   */
  private String value;

  /**
   * 是否禁用.
   */
  private Boolean disabled;

  /**
   * 关联的字典.
   */
  @NotNull
  private Long dictId;

  /**
   * 排序.
   */
  private Integer orderNo;
}
