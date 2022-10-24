package rocket.starter.message.domain;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import rocket.starter.common.BaseEntity;

/**
 * message history.
 *
 * @author herong 2021/2/9
 */
@Getter
@Setter
@Entity
@Table(name = "message_history", indexes = @Index(name = "idx_receiver", columnList = "receiver"))
public class MessageHistory extends BaseEntity {

  /**
   * 发送人用户名.
   */
  private String sender;

  /**
   * 接受人用户名.
   */
  private String receiver;

  /**
   * 消息类型.
   */
  private MessageType type;

  /**
   * 发送内容.
   */
  private String content;

  /**
   * 发送时间.
   */
  private LocalDateTime sendTime;

  /**
   * 查看情况.
   */
  @ElementCollection
  @CollectionTable(name = "message_history_read")
  private Set<ReadInfo> reads;
}
