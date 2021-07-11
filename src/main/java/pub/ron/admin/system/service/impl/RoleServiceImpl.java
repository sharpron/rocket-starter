package pub.ron.admin.system.service.impl;

import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.repo.RoleRepo;
import pub.ron.admin.system.service.RoleService;

/**
 * role service impl.
 *
 * @author ron 2020/11/22
 */
@Service
public class RoleServiceImpl extends AbstractService<Role, RoleRepo> implements RoleService {

  public RoleServiceImpl(RoleRepo repository) {
    super(repository);
  }
}
