package rocket.starter.message.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 发布消息.
 *
 * @author ron 2022/8/30
 */
@Data
public class PublishMessage {

  @NotBlank
  @Size(max = 255)
  private String content;
}
