package rocket.starter.property.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import rocket.starter.common.BaseEntity;

/**
 * 系统属性.
 *
 * @author ron 2021/10/21
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor
@Table(
    name = "sys_property",
    indexes = {@Index(name = "idx_key", columnList = "referenceKey")})
public class Property extends BaseEntity {

  @NotBlank(message = "引用名称")
  @Column(updatable = false)
  private String referenceKey;

  /**
   * 值.
   */
  @Size(max = 255)
  private String value;

  /**
   * 值类型.
   */
  @NotNull
  private ValueType valueType;

  /**
   * 描述.
   */
  @NotBlank
  @Size(max = 255)
  private String description;

}