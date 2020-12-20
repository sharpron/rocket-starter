package pub.ron.admin.system.service.impl;

import pub.ron.admin.common.AppException;
import pub.ron.admin.system.body.DeptBody;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.dto.DeptDto;
import pub.ron.admin.system.dto.DeptNode;
import pub.ron.admin.system.repo.DeptRepo;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.service.DeptService;
import pub.ron.admin.system.service.mapper.DeptMapper;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ron 2020/11/19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeptServiceImpl implements DeptService {

  private static final String DEPT_PATH_SEPARATOR = "/";

  private final DeptRepo deptRepo;

  private final DeptMapper deptMapper;

  @Override
  public List<DeptNode> findAsTree() {
    return findAsTree(deptMapper::mapNode, DeptNode::getChildren);
  }

  @Override
  public Page<DeptDto> findFullAsTree() {
    final List<DeptDto> asTree = findAsTree(deptMapper::mapDto, DeptDto::getChildren);
    return new PageImpl<>(
        asTree,
        Pageable.unpaged(),
        asTree.size()
    );
  }

  private <T> List<T> findAsTree(
      Function<Dept, T> mapping, Function<T, List<T>> childrenGetter) {
    final UserPrincipal principal = SubjectUtils.currentUser();
    final List<Dept> childrenByPath = deptRepo.findByPath(
        principal.getDeptPath()
    );

    Dept root = null;
    for (int i = 0; i < childrenByPath.size(); i++) {
      final Dept dept = childrenByPath.get(i);
      if (dept.getId().equals(principal.getDeptId())) {
        root = childrenByPath.remove(i);
        break;
      }
    }
    assert root != null;
    final T rootNode = mapping.apply(root);
    genTreeByPath(childrenByPath, root.getId(),
        childrenGetter.apply(rootNode), mapping, childrenGetter);
    return Collections.singletonList(rootNode);
  }

  private <R> void genTreeByPath(
      List<Dept> departments,
      Long parentId,
      List<R> children,
      Function<Dept, R> mapping,
      Function<R, List<R>> childrenGetter) {

    final Iterator<Dept> iterator = departments.iterator();
    while (iterator.hasNext()) {
      final Dept next = iterator.next();

      if (next.getParent().getId().equals(parentId)) {
        final R node = mapping.apply(next);
        log.debug("mapped {} to node {}", next, node);
        children.add(node);
        iterator.remove();
        genTreeByPath(departments, next.getId(),
            childrenGetter.apply(node), mapping, childrenGetter);
      }
    }
  }

  @Override
  @Transactional
  public void add(DeptBody deptBody) {

    final Dept dept = deptMapper.mapDept(deptBody);
    dept.setPath(buildPath(deptBody.getParentId()));
    deptRepo.save(dept);
  }

  String buildPath(Long parentId) {
    return deptRepo.getPath(parentId) + parentId + DEPT_PATH_SEPARATOR;
  }

  @Override
  @Transactional
  public void update(Long id, DeptBody deptBody) {
    final Dept dept = deptMapper.mapDept(deptBody);
    dept.setId(id);
    deptRepo.save(dept);
  }

  @Override
  public void remove(Long id) {
    if (deptRepo.countByParent_Id(id) > 0) {
      throw new AppException("存在子部门，不能删除");
    }
    deptRepo.deleteById(id);
  }

}
