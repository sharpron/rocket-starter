package rocket.starter.common;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * data transfer object 与{@link BaseEntity}一一对应.
 *
 * @author ron 2020/11/17
 */
@Getter
@Setter
public abstract class BaseDto {

  private Long id;

  private String createBy;

  private LocalDateTime createTime;

  private String modifyBy;

  private LocalDateTime modifyTime;

  /**
   * 设置base dto字段.
   *
   * @param baseEntity 实体
   */
  protected void setBase(BaseEntity baseEntity) {
    this.id = baseEntity.getId();
    this.createBy = baseEntity.getCreateBy();
    this.createTime = baseEntity.getCreateTime();
    this.modifyBy = baseEntity.getModifyBy();
    this.modifyTime = baseEntity.getModifyTime();
  }
}
