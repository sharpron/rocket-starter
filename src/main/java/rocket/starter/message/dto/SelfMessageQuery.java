package rocket.starter.message.dto;

import java.util.Set;
import lombok.Data;
import rocket.starter.common.query.Where;
import rocket.starter.common.query.Where.Type;
import rocket.starter.message.domain.MessageType;

/**
 * message query.
 *
 * @author herong 2022/8/30
 */
@Data
public class SelfMessageQuery {

  @Where(type = Type.in)
  private Set<String> receiver;

  @Where
  private MessageType messageType;
}
