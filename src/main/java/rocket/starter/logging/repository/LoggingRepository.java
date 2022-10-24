package rocket.starter.logging.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.logging.domain.Logging;
import rocket.starter.logging.domain.Status;

/**
 * logging repository.
 *
 * @author ron 2020/12/12
 */
@Repository
public interface LoggingRepository extends BaseRepo<Logging> {

  /**
   * 通过状态删除日志.
   *
   * @param status 状态
   */
  @Modifying
  @Query("delete from Logging where status=?1")
  void deleteByStatus(Status status);
}
