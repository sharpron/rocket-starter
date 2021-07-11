package pub.ron.admin.system.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.dto.DeptDto;
import pub.ron.admin.system.dto.DeptNode;
import pub.ron.admin.system.repo.DeptRepo;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.service.DeptService;
import pub.ron.admin.system.service.mapper.DeptMapper;

/**
 * dept service impl.
 *
 * @author ron 2020/11/19
 */
@Service
@Slf4j
public class DeptServiceImpl extends AbstractService<Dept, DeptRepo> implements DeptService {

  private static final String DEPT_PATH_SEPARATOR = "/";

  private final DeptMapper deptMapper;

  public DeptServiceImpl(DeptRepo deptRepo, DeptMapper deptMapper) {
    super(deptRepo);
    this.deptMapper = deptMapper;
  }

  private <T> List<T> findAsTree(Function<Dept, T> mapping, Function<T, List<T>> childrenGetter) {
    final UserPrincipal principal = SubjectUtils.currentUser();
    final List<Dept> childrenByPath = repository.findByPath(principal.getDeptPath());

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
    genTreeByPath(
        childrenByPath, root.getId(), childrenGetter.apply(rootNode), mapping, childrenGetter);
    return Collections.singletonList(rootNode);
  }

  @Override
  public List<DeptNode> findAsTree() {
    return findAsTree(deptMapper::mapNode, DeptNode::getChildren);
  }

  @Override
  public Page<DeptDto> findFullAsTree() {
    final List<DeptDto> asTree = findAsTree(deptMapper::mapDto, DeptDto::getChildren);
    return new PageImpl<>(asTree, Pageable.unpaged(), asTree.size());
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
        genTreeByPath(
            departments, next.getId(), childrenGetter.apply(node), mapping, childrenGetter);
      }
    }
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
}
