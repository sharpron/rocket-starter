package rocket.starter.system.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Dept;
import rocket.starter.system.domain.Role;
import rocket.starter.system.repo.DeptRepo;
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
  private final DeptRepo deptRepo;

  @Override
  public Set<Long> findRolesByUserId(Long userId) {
    return roleRepo.findRolesByUserId(userId);
  }

  @Override
  public Set<Long> findManageDeptIds(Set<Long> roleIds) {
    // 直接管理的部门
    Set<String> manageDeptPaths = roleRepo.findManageDeptPaths(roleIds);
    Set<Long> result = new HashSet<>();
    for (String manageDeptPath : manageDeptPaths) {
      List<Dept> byPath = deptRepo.findByPath(manageDeptPath);
      for (Dept dept : byPath) {
        result.add(dept.getId());
      }
    }
    return result;
  }

  @Override
  protected BaseRepo<Role> getBaseRepo() {
    return roleRepo;
  }
}
