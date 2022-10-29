package rocket.starter.system.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Role;
import rocket.starter.system.repo.RoleRepo;
import rocket.starter.system.service.RoleService;

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
