package pub.ron.admin.message.service;

import java.util.EnumSet;
import pub.ron.admin.message.SendType;
import pub.ron.admin.message.domain.MessageHistory;

/**
 * message service.
 *
 * @author herong 2021/2/9
 */
public interface MessageService {

  /**
   * 发送消息.
   *
   * @param message   消息
   * @param sendTypes 发送类型
   */
  void send(MessageHistory message, EnumSet<SendType> sendTypes);
}
