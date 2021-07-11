package pub.ron.admin.logging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pub.ron.admin.logging.domain.Logging;

/**
 * logging repository.
 *
 * @author ron 2020/12/12
 */
@Repository
public interface LoggingRepository
    extends JpaRepository<Logging, Long>, JpaSpecificationExecutor<Logging> {}
