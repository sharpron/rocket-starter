package pub.ron.admin.message.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pub.ron.admin.message.domain.MessageHistory;

/**
 * repository for message history.
 *
 * @author herong 2021/2/9
 */
@Repository
public interface MessageHistoryRepo
    extends JpaRepository<MessageHistory, Long>, JpaSpecificationExecutor<MessageHistory> {

  void deleteByCreateBy(String createBy);
}
