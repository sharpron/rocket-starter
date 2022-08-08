package pub.ron.admin.system.service;

import java.util.List;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.Dept;

/**
 * dept service.
 *
 * @author ron 2020/11/18
 */
public interface DeptService extends BaseService<Dept> {

  List<Dept> findSelfDepartments();
}
