package pub.ron.admin.message;

/**
 * 消息传输器
 *
 * @author herong 2021/2/9
 */
public interface Transmitter {

  /**
   * 传输消息
   *
   * @param message 消息
   */
  void transmit(Message message);

}
