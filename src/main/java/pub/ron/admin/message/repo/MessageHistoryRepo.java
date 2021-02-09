package pub.ron.admin.message.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pub.ron.admin.message.domain.MessageHistory;

/**
 * @author herong 2021/2/9
 */
@Repository
public interface MessageHistoryRepo extends JpaRepository<MessageHistory, Long>,
    JpaSpecificationExecutor<MessageHistory> {

  int deleteByCreateBy(String createBy);

}
