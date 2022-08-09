package pub.ron.admin.system.repo;

import java.util.List;
import org.springframework.stereotype.Repository;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.DictItem;

/**
 * dict item repository.
 *
 * @author ron 2022/8/9
 */
@Repository
public interface DictItemRepo extends BaseRepo<DictItem> {

  List<DictItem> findByDictId(Long dictId);

}
