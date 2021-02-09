package pub.ron.admin.message.transmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pub.ron.admin.message.Message;
import pub.ron.admin.message.Transmitter;

/**
 * email方式发送消息
 *
 * @author herong 2021/2/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTransmitter implements Transmitter {

  private final JavaMailSender javaMailSender;

  @Override
  public void transmit(Message message) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(message.getReceiverContact());
    simpleMailMessage.setFrom(message.getSenderContact());
    simpleMailMessage.setSubject(message.getTitle());
    simpleMailMessage.setText(message.getContent());
    try {
      javaMailSender.send(simpleMailMessage);
    } catch (Exception e) {
      log.error("发送失败", e);
    }
  }
}
