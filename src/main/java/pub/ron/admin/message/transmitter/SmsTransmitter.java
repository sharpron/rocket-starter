package pub.ron.admin.message.transmitter;

import org.springframework.stereotype.Component;
import pub.ron.admin.message.Message;
import pub.ron.admin.message.Transmitter;

/**
 * 短信方式发送消息
 *
 * @author herong 2021/2/9
 */
@Component
public class SmsTransmitter implements Transmitter {

  @Override
  public void transmit(Message message) {
    throw new UnsupportedOperationException("短信发送方式未实现");
  }
}
