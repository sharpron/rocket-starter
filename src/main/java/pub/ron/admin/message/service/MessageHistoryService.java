package pub.ron.admin.message.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.admin.message.domain.MessageHistory;
import pub.ron.admin.message.dto.MessageHistoryQuery;

/**
 * message history.
 *
 * @author herong 2021/2/9
 */
public interface MessageHistoryService {

  /**
   * 分页查询.
   *
   * @param pageable 分页器
   * @param query    查询条件
   * @return 分页结果
   */
  Page<MessageHistory> findByPage(Pageable pageable, MessageHistoryQuery query);

  /**
   * 添加发送消息的历史记录.
   *
   * @param history 历史记录
   */
  void addHistory(MessageHistory history);

  /**
   * 清空某个人的消息历史.
   *
   * @param username 用户名
   */
  void deleteByUsername(String username);

  /**
   * 清空某个人的消息.
   *
   * @param id id
   */
  void deleteById(Long id);
}
