package rocket.starter.system.repo;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Dict;

/**
 * dict repository.
 *
 * @author ron 2022/8/8
 */
@Repository
public interface DictRepo extends BaseRepo<Dict> {

  /**
   * 通过引用名称查询字典.
   *
   * @param name 名称
   * @return 字典
   */
  Optional<Dict> findByName(String name);
}
