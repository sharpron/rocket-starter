package pub.ron.admin.quartz.repo;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.quartz.domain.QuartzJob;

/**
 * @author herong 2021/2/9
 */
@Repository
public interface QuartzJobRepo extends BaseRepo<QuartzJob> {

  List<QuartzJob> findByEnabledIs(Boolean enabled);

  @Query("update QuartzJob set enabled=?2 where id=?1 and enabled<>?2")
  @Modifying
  int setEnabled(Long id, boolean enabled);
}
