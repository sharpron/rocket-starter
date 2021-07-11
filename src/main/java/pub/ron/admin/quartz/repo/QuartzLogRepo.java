package pub.ron.admin.quartz.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pub.ron.admin.quartz.domain.QuartzLog;

/**
 * quartz log repository.
 *
 * @author herong 2021/2/9
 */
@Repository
public interface QuartzLogRepo
    extends JpaRepository<QuartzLog, Long>, JpaSpecificationExecutor<QuartzLog> {}
