package pub.ron.admin.message.service.impl;

import java.time.LocalDateTime;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.message.body.ChatMessage;
import pub.ron.admin.message.body.PublishMessage;
import pub.ron.admin.message.domain.MessageHistory;
import pub.ron.admin.message.domain.MessageType;
import pub.ron.admin.message.dto.SelfMessageQuery;
import pub.ron.admin.message.repo.MessageHistoryRepo;
import pub.ron.admin.message.service.MessageHistoryService;
import pub.ron.admin.message.service.mapper.MessageHistoryMapper;
import pub.ron.admin.system.security.SubjectUtils;

/**
 * message history service.
 *
 * @author herong 2021/2/9
 */
@Service
@RequiredArgsConstructor
public class MessageHistoryServiceImpl extends AbstractService<MessageHistory> implements
    MessageHistoryService {

  private static final String RECEIVER_ALL = "ALL";

  private final MessageHistoryRepo messageHistoryRepo;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final MessageHistoryMapper messageHistoryMapper;

  @Override
  public void publishMessage(PublishMessage publishMessage) {
    LocalDateTime now = LocalDateTime.now();

    MessageHistory messageHistory = new MessageHistory();
    messageHistory.setSender(SubjectUtils.currentUser().getUsername());
    messageHistory.setReceiver(RECEIVER_ALL);
    messageHistory.setType(MessageType.PUBLISH);
    messageHistory.setContent(publishMessage.getContent());
    messageHistory.setSendTime(now);
    messageHistoryRepo.save(messageHistory);

    simpMessagingTemplate.convertAndSend("/topic/messages",
        messageHistoryMapper.mapSmall(messageHistory));
  }

  @Override
  public void sendMessage(ChatMessage chatMessage) {
    LocalDateTime now = LocalDateTime.now();

    MessageHistory messageHistory = new MessageHistory();
    messageHistory.setSender(SubjectUtils.currentUser().getUsername());
    messageHistory.setReceiver(chatMessage.getReceiver());
    messageHistory.setType(MessageType.SEND);
    messageHistory.setContent(chatMessage.getContent());
    messageHistory.setSendTime(now);
    messageHistoryRepo.save(messageHistory);

    simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver(),
        "/queue/messages", messageHistoryMapper.mapSmall(messageHistory));
  }

  @Override
  protected BaseRepo<MessageHistory> getBaseRepo() {
    return messageHistoryRepo;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void updateRead(Set<Long> ids) {
    String username = SubjectUtils.currentUser().getUsername();
    LocalDateTime now = LocalDateTime.now();
    for (Long id : ids) {
      messageHistoryRepo.insertReadInfo(id, now, username);
    }
  }

  @Override
  public void readAll() {
    String username = SubjectUtils.currentUser().getUsername();
    messageHistoryRepo.insertReadInfo(username, LocalDateTime.now());
  }

  @Override
  public Long getUnreadCount() {
    String username = SubjectUtils.currentUser().getUsername();
    return messageHistoryRepo.countUnreadByReceiverIn(Set.of(RECEIVER_ALL, username));
  }

  @Override
  public Page<MessageHistory> findSelfByPage(Pageable pageable) {
    SelfMessageQuery selfMessageQuery = new SelfMessageQuery();
    String username = SubjectUtils.currentUser().getUsername();
    selfMessageQuery.setReceiver(Set.of(RECEIVER_ALL, username));
    return findByPage(pageable, selfMessageQuery);
  }

}
