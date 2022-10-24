package rocket.starter.system.service;

import java.util.Set;
import rocket.starter.common.BaseService;
import rocket.starter.system.domain.Role;

/**
 * role service.
 *
 * @author ron 2020/11/18
 */
public interface RoleService extends BaseService<Role> {

  Set<Long> findManageDeptIds(Long userId);
}
