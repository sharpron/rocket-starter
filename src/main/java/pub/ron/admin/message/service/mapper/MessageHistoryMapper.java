package pub.ron.admin.message.service.mapper;

import java.util.Objects;
import org.springframework.stereotype.Component;
import pub.ron.admin.message.domain.MessageHistory;
import pub.ron.admin.message.domain.ReadInfo;
import pub.ron.admin.message.dto.MessageSmallDto;
import pub.ron.admin.system.security.SubjectUtils;

/**
 * 消息mapper.
 *
 * @author ron 2022/8/30
 */
@Component
public class MessageHistoryMapper {

  /**
   * 转换到small.
   *
   * @param messageHistory messageHistory
   * @return small
   */
  public MessageSmallDto mapSmall(MessageHistory messageHistory) {
    MessageSmallDto messageSmallDto = new MessageSmallDto();
    messageSmallDto.setId(messageHistory.getId());
    messageSmallDto.setSender(messageHistory.getSender());
    messageSmallDto.setContent(messageHistory.getContent());
    messageSmallDto.setType(messageHistory.getType());
    messageSmallDto.setSendTime(messageHistory.getSendTime());
    String username = SubjectUtils.currentUser().getUsername();

    if (messageHistory.getReads() != null) {
      messageSmallDto.setRead(messageHistory.getReads().stream()
          .map(ReadInfo::getUser).anyMatch(e -> Objects.equals(e, username)));
    }
    return messageSmallDto;
  }
}
