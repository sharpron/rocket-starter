package pub.ron.admin.system.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.repo.RoleRepo;
import pub.ron.admin.system.service.RoleService;

/**
 * role service impl.
 *
 * @author ron 2020/11/22
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {

  private final RoleRepo roleRepo;

  @Override
  public Set<Long> findManageDeptIds(Long userId) {
    return roleRepo.findManageDeptIds(userId);
  }

  @Override
  protected BaseRepo<Role> getBaseRepo() {
    return roleRepo;
  }
}
