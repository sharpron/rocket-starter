package rocket.starter.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * base repo.
 *
 * @author ron 2021/1/1
 */
@NoRepositoryBean
public interface BaseRepo<T extends BaseEntity>
    extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

}
