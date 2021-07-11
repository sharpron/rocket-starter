package pub.ron.admin.message.service.impl;

import java.time.LocalDateTime;
import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pub.ron.admin.message.Message;
import pub.ron.admin.message.SendType;
import pub.ron.admin.message.domain.MessageHistory;
import pub.ron.admin.message.service.MessageHistoryService;
import pub.ron.admin.message.service.MessageService;
import pub.ron.admin.message.transmitter.EmailTransmitter;
import pub.ron.admin.message.transmitter.SmsTransmitter;
import pub.ron.admin.message.transmitter.WebSocketTransmitter;

/**
 * message service.
 *
 * @author herong 2021/2/9
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageHistoryService messageHistoryService;
  private final EmailTransmitter emailTransmitter;
  private final SmsTransmitter smsTransmitter;
  private final WebSocketTransmitter webSocketTransmitter;

  @Override
  public void send(MessageHistory messageHistory, EnumSet<SendType> sendTypes) {

    final Message message =
        new Message(
            messageHistory.getSenderContact(),
            messageHistory.getReceiverContact(),
            messageHistory.getTitle(),
            messageHistory.getContent(),
            LocalDateTime.now());

    for (SendType sendType : sendTypes) {
      switch (sendType) {
        case SMS:
          smsTransmitter.transmit(message);
          break;
        case EMAIL:
          emailTransmitter.transmit(message);
          break;
        default:
          throw new AssertionError();
      }
    }
    webSocketTransmitter.transmit(message);
    messageHistoryService.addHistory(messageHistory);
  }
}
