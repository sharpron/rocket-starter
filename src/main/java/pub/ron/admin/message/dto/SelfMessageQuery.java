package pub.ron.admin.message.dto;

import java.util.Set;
import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;
import pub.ron.admin.message.domain.MessageType;

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
