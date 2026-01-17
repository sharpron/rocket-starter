package rocket.starter.system.service;

import java.util.List;
import rocket.starter.common.BaseService;
import rocket.starter.system.domain.Dept;
import rocket.starter.system.dto.DeptQuery;

/**
 * dept service.
 *
 * @author ron 2020/11/18
 */
public interface DeptService extends BaseService<Dept> {

  List<Dept> findSelfDepartments(DeptQuery deptQuery);
}
