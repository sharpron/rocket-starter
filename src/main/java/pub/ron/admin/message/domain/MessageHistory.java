package pub.ron.admin.message.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseEntity;
import pub.ron.admin.message.SendType;

/**
 * @author herong 2021/2/9
 */
@Getter
@Setter
@Entity
@Table(name = "message_history")
public class MessageHistory extends BaseEntity {

  /**
   * 发送人用户名
   */
  private String sender;

  /**
   * 发送人联系方式
   */
  private String senderContact;

  /**
   * 接受人联系方式
   */
  private String receiverContact;

  /**
   * 接受人用户名
   */
  private String receiver;

  /**
   * 标题
   */
  private String title;

  /**
   * 内容
   */
  private String content;

  /**
   * 发送类型
   */
  private SendType sendType;

  /**
   * 标记消息是否已经被收件人阅读
   */
  private boolean read;
}
