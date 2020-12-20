package pub.ron.admin.logging.repository;

import pub.ron.admin.logging.domain.Logging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ron 2020/12/12
 */
@Repository
public interface LoggingRepository extends JpaRepository<Logging, Long> {

}
