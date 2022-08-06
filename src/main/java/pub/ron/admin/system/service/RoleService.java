package pub.ron.admin.system.service;

import java.util.Set;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.Role;

/**
 * role service.
 *
 * @author ron 2020/11/18
 */
public interface RoleService extends BaseService<Role> {

  Set<Long> findManageDeptIds(Long userId);
}
