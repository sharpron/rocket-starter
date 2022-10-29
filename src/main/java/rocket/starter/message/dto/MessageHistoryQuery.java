package rocket.starter.message.dto;

import lombok.Data;
import rocket.starter.common.query.Where;
import rocket.starter.common.query.Where.Type;
import rocket.starter.message.domain.MessageType;

/**
 * message query.
 *
 * @author herong 2021/2/9
 */
@Data
public class MessageHistoryQuery {

  @Where(type = Type.eq)
  private String sender;

  @Where(type = Type.eq)
  private String receiver;

  @Where(type = Type.eq)
  private MessageType type;
}
