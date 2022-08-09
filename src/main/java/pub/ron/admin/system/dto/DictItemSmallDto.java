package pub.ron.admin.system.dto;

import lombok.Data;

/**
 * 字典项目.
 *
 * @author ron 2022/8/8
 */
@Data
public class DictItemSmallDto {

  private Long id;

  /**
   * 名称.
   */
  private String name;

  /**
   * 是否禁用.
   */
  private Boolean disabled;


}
