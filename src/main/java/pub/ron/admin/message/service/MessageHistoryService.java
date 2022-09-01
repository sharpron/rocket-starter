package pub.ron.admin.message.service;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.message.domain.MessageHistory;

/**
 * message history.
 *
 * @author herong 2021/2/9
 */
public interface MessageHistoryService extends BaseService<MessageHistory>, MessageService {

  /**
   * 更新读取状态.
   *
   * @param ids ids
   */
  void updateRead(Set<Long> ids);

  void readAll();

  Long getUnreadCount();

  Page<MessageHistory> findSelfByPage(Pageable pageable);
}
