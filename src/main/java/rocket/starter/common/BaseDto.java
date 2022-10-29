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

}
