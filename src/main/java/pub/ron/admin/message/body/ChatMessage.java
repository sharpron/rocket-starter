package pub.ron.admin.message.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 聊天消息.
 *
 * @author ron 2022/8/30
 */
@Data
public class ChatMessage {

  /**
   * 接收人.
   */
  @NotBlank
  @Size(max = 255)
  private String receiver;

  /**
   * 内容.
   */
  @NotBlank
  @Size(max = 255)
  private String content;
}
