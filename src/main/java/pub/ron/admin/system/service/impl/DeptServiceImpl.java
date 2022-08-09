package pub.ron.admin.system.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.domain.Dept;
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
  protected void beforeCreate(Dept dept) {
    updatePath(dept);
  }

  @Override
  protected void beforeUpdate(Dept dept) {
    updatePath(dept);
  }

  private void updatePath(Dept dept) {
    final Long parentId = dept.getParent().getId();
    final String path = deptRepo.getPath(parentId) + parentId + DEPT_PATH_SEPARATOR;
    dept.setPath(path);
  }

  @Override
  protected void beforeDelete(Dept dept) {
    final List<Dept> children = deptRepo.findByPath(dept.getPath());
    deptRepo.deleteAll(children);
  }

  @Override
  public List<Dept> findSelfDepartments(DeptQuery deptQuery) {
    final Principal principal = SubjectUtils.currentUser();
    deptQuery.setPath(principal.getDeptPath());
    return findAll(deptQuery);
  }
}
