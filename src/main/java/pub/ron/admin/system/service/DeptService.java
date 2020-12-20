package pub.ron.admin.system.service;

import pub.ron.admin.system.body.DeptBody;
import pub.ron.admin.system.dto.DeptDto;
import pub.ron.admin.system.dto.DeptNode;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @author ron 2020/11/18
 */
public interface DeptService {

  List<DeptNode> findAsTree();

  Page<DeptDto> findFullAsTree();

  void add(DeptBody deptBody);

  void update(Long id, DeptBody deptBody);

  void remove(Long id);

}
