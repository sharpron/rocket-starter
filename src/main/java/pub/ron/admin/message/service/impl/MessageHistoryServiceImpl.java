package pub.ron.admin.message.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.query.WhereBuilder;
import pub.ron.admin.message.domain.MessageHistory;
import pub.ron.admin.message.dto.MessageHistoryQuery;
import pub.ron.admin.message.repo.MessageHistoryRepo;
import pub.ron.admin.message.service.MessageHistoryService;

/**
 * @author herong 2021/2/9
 */
@Service
@RequiredArgsConstructor
public class MessageHistoryServiceImpl implements MessageHistoryService {

  private final MessageHistoryRepo messageHistoryRepo;

  @Override
  public Page<MessageHistory> findByPage(Pageable pageable, MessageHistoryQuery query) {
    return messageHistoryRepo.findAll(WhereBuilder.buildSpec(query), pageable);
  }

  @Override
  public void addHistory(MessageHistory history) {
    messageHistoryRepo.save(history);
  }

  @Override
  public void deleteByUsername(String username) {
    messageHistoryRepo.deleteByCreateBy(username);
  }

  @Override
  public void deleteById(Long id) {
    messageHistoryRepo.deleteById(id);
  }
}
