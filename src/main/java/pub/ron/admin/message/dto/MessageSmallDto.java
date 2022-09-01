package pub.ron.admin.message.dto;

import java.time.LocalDateTime;
import lombok.Data;
import pub.ron.admin.message.domain.MessageType;

/**
 * 消息传输对象.
 *
 * @author ron 2022/8/30
 */
@Data
public class MessageSmallDto {

  private Long id;
  private String sender;
  private String content;
  private MessageType type;
  private LocalDateTime sendTime;
  private boolean read;

}
