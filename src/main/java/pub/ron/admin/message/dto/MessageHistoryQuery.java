package pub.ron.admin.message.dto;

import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;

/**
 * @author herong 2021/2/9
 */
@Data
public class MessageHistoryQuery {

  @Where(type = Type.eq)
  private String receiver;

}
