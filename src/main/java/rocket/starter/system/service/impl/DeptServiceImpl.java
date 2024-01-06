package rocket.starter.system.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.AppException;
import rocket.starter.common.BaseRepo;
import rocket.starter.system.domain.Dept;
import rocket.starter.system.domain.User;
import rocket.starter.system.dto.DeptQuery;
import rocket.starter.system.repo.DeptRepo;
import rocket.starter.system.security.Principal;
import rocket.starter.system.security.SubjectUtils;
import rocket.starter.system.service.DeptService;

/**
 * dept service impl.
 *
 * @author ron 2020/11/19
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DeptServiceImpl extends AbstractService<Dept> implements DeptService {

  private static final String DEPT_PATH_SEPARATOR = "/";

  private final DeptRepo deptRepo;


  @Override
  protected BaseRepo<Dept> getBaseRepo() {
    return deptRepo;
  }

  @Override
  protected void afterCreate(Dept dept) {
    dept.setPath(generatePath(dept));
  }

  @Override
  protected void beforeUpdate(Dept dept) {
    if (dept.getParent() != null && dept.getId().equals(dept.getParent().getId())) {
      throw new AppException("不能将自身设置为上级部门");
    }
    dept.setPath(generatePath(dept));
  }

  private String generatePath(Dept dept) {
    Dept parent = dept.getParent();

    String parentPath = parent == null ? DEPT_PATH_SEPARATOR :
        deptRepo.getPath(parent.getId());

    return parentPath + dept.getId() + DEPT_PATH_SEPARATOR;
  }

  @Override
  protected void beforeDelete(Dept dept) {
    final List<Dept> children = deptRepo.findByPath(dept.getPath());
    deptRepo.deleteAll(children);
  }

  @Override
  public List<Dept> findSelfDepartments(DeptQuery deptQuery) {
    final Principal principal = SubjectUtils.currentUser();
    if (User.ADMIN.equals(principal.getUsername())) {
      return findAll(deptQuery);
    }
    deptQuery.setPath(principal.getDeptPath());
    return findAll(deptQuery);
  }
}
