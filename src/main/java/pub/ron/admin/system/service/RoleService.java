package pub.ron.admin.system.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.dto.RoleDto;
import pub.ron.admin.system.dto.RoleQuery;
import pub.ron.admin.system.dto.SimpleRoleDto;

/**
 * @author ron 2020/11/18
 */
public interface RoleService {

  void create(RoleBody roleBody);

  void update(Long id, RoleBody roleBody);

  Page<RoleDto> findByPage(Pageable pageable, RoleQuery roleQuery);

  List<SimpleRoleDto> findAll();

  void remove(Long id);
}
