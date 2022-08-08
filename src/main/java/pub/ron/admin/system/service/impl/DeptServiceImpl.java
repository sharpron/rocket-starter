package pub.ron.admin.system.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.system.domain.Dept;
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
public class DeptServiceImpl extends AbstractService<Dept, DeptRepo> implements DeptService {

  private static final String DEPT_PATH_SEPARATOR = "/";

  public DeptServiceImpl(DeptRepo deptRepo) {
    super(deptRepo);
  }


  @Override
  public void create(Dept dept) {
    updatePath(dept);
    super.create(dept);
  }

  @Override
  public void update(Dept dept) {
    updatePath(dept);
    super.update(dept);
  }

  private void updatePath(Dept dept) {
    final Long parentId = dept.getParent().getId();
    final String path = repository.getPath(parentId) + parentId + DEPT_PATH_SEPARATOR;
    dept.setPath(path);
  }

  @Override
  public void deleteById(Long id) {
    final String path = repository.getPath(id);
    final List<Dept> children = repository.findByPath(path);
    repository.deleteAll(children);
  }

  @Override
  public List<Dept> findSelfDepartments() {
    final Principal principal = SubjectUtils.currentUser();
    return repository.findByPath(principal.getDeptPath());
  }
}
