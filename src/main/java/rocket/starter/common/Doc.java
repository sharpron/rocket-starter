package rocket.starter.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文件.
 *
 * @author ron 2022/8/9
 */
@Data
@Embeddable
public class Doc {

  @NotNull
  private String docName;

  @NotNull
  private String docPath;
}
