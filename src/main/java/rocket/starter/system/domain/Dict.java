package rocket.starter.system.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import rocket.starter.common.BaseEntity;

/**
 * 字典表.
 *
 * @author ron 2022/8/8
 */
@Getter
@Setter
@Entity
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

  /**
   * 引用名称.
   */
  private String name;

  /**
   * 字典描述.
   */
  private String description;

}
