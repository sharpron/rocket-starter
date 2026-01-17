package rocket.starter.quartz.repo;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.quartz.domain.QuartzJob;

/**
 * quartz job repository.
 *
 * @author herong 2021/2/9
 */
@Repository
public interface QuartzJobRepo extends BaseRepo<QuartzJob> {

  List<QuartzJob> findByEnabledIs(Boolean enabled);

  @Query("update QuartzJob set enabled=?2 where id=?1 and enabled<>?2")
  @Modifying
  int updateEnabled(Long id, boolean enabled);
}
