package pub.ron.admin.system.service;

import java.util.List;
import org.springframework.data.domain.Page;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.dto.DeptDto;
import pub.ron.admin.system.dto.DeptNode;

/**
 * @author ron 2020/11/18
 */
public interface DeptService extends BaseService<Dept> {

  List<DeptNode> findAsTree();

  Page<DeptDto> findFullAsTree();

}
