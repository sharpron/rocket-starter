package rocket.starter.system.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rocket.starter.common.BaseEntity;

/**
 * 字典项目表.
 *
 * @author ron 2022/8/8
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sys_dict_item", indexes = @Index(name = "idx_dict_id", columnList = "dictId"))
public class DictItem extends BaseEntity {

  /**
   * 名称.
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
  private Long dictId;

  /**
   * 排序.
   */
  private Integer orderNo;

  /**
   * 设置变量.
   */
  @PrePersist
  public void prePersist() {
    if (this.disabled == null) {
      this.disabled = false;
    }
  }

  public DictItem(Long id) {
    super(id);
  }


}
