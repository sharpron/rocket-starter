package pub.ron.admin.message;

import java.time.LocalDateTime;
import lombok.Value;

/**
 * message.
 *
 * @author herong 2021/2/9
 */
@Value
public class Message {

  /**
   * 发送人联系方式.
   */
  String senderContact;

  /**
   * 接受人联系方式.
   */
  String receiverContact;

  /**
   * 标题.
   */
  String title;

  /**
   * 内容.
   */
  String content;

  /**
   * 发送时间.
   */
  LocalDateTime time;
}
