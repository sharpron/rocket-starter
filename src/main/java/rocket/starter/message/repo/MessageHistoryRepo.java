package rocket.starter.message.repo;

import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.message.domain.MessageHistory;

/**
 * repository for message history.
 *
 * @author herong 2021/2/9
 */
@Repository
public interface MessageHistoryRepo extends BaseRepo<MessageHistory> {

  @Query(value = "insert into message_history_read values(?1, ?2, ?3)", nativeQuery = true)
  @Modifying
  void insertReadInfo(Long id, LocalDateTime time, String user);

  @Query(value = "insert into message_history_read select id, ?2, ?1 from message_history",
      nativeQuery = true)
  @Modifying
  void insertReadInfo(String user, LocalDateTime time);

  /**
   * 通过接收人查询消息数量.
   *
   * @param receivers 接收人
   * @return 数量
   */
  @Query(value = "select count(*) from message_history where id not in (select message_history_id "
      + "from message_history_read) and receiver in ?1", nativeQuery = true)
  long countUnreadByReceiverIn(Set<String> receivers);
}
