package pub.ron.admin.system.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseEntity;

/**
 * 字典项目表.
 *
 * @author ron 2022/8/8
 */
@Getter
@Setter
@Entity
@Table(name = "sys_dict_item", indexes = @Index(name = "idx_dict_id", columnList = "dictId"))
public class DictItem extends BaseEntity {

  /**
   * 引用名称.
   */
  private String name;

  /**
   * 引用的值.
   */
  private String value;

  /**
   * 关联的字典.
   */
  private Long dictId;

  /**
   * 排序.
   */
  private Integer orderNo;

  /**
   * 字典描述.
   */
  private String description;

}
