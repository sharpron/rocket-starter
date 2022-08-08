package pub.ron.admin.system.repo;

import org.springframework.stereotype.Repository;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.domain.Dict;

/**
 * dict repository.
 *
 * @author ron 2022/8/8
 */
@Repository
public interface DictRepo extends BaseRepo<Dict> {

}
