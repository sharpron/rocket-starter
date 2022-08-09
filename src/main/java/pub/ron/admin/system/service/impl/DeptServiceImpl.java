package pub.ron.admin.system.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.DeptQuery;
import pub.ron.admin.system.repo.DeptRepo;
import pub.ron.admin.system.security.Principal;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.DeptService;

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
