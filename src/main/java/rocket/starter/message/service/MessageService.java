package rocket.starter.message.service;

import rocket.starter.message.body.ChatMessage;
import rocket.starter.message.body.PublishMessage;

/**
 * message service.
 *
 * @author herong 2021/2/9
 */
public interface MessageService {

  /**
   * 发布消息给所有人.
   *
   * @param publishMessage 发布消息
   */
  void publishMessage(PublishMessage publishMessage);

  /**
   * 发送消息给某人.
   *
   * @param chatMessage 聊天消息
   */
  void sendMessage(ChatMessage chatMessage);
}
