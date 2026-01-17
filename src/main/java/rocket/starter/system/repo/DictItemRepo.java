package rocket.starter.system.repo;

import java.util.List;
import org.springframework.stereotype.Repository;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.DictItem;

/**
 * dict item repository.
 *
 * @author ron 2022/8/9
 */
@Repository
public interface DictItemRepo extends BaseRepo<DictItem> {

  List<DictItem> findByDictId(Long dictId);

}
