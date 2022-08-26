package pub.ron.admin.system.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pub.ron.admin.common.BaseEntity;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.domain.Menu;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.dto.RoleDto;

/**
 * 角色映射.
 *
 * @author ron 2022/8/12
 */
@Component
public class RoleMapper {


  /**
   * 转换成dto.
   *
   * @param role role
   * @return dto
   */
  public RoleDto mapDto(Role role) {
    RoleDto roleDto = new RoleDto();
    roleDto.setName(role.getName());
    roleDto.setDisabled(role.getDisabled());
    roleDto.setDescription(role.getDescription());

    Set<Dept> departments = role.getDepartments();
    roleDto.setDeptIds(departments.stream()
        .map(BaseEntity::getId)
        .collect(Collectors.toSet()));

    Set<Menu> menus = role.getMenus();
    roleDto.setMenuIds(menus.stream().map(BaseEntity::getId).collect(Collectors.toSet()));

    roleDto.setId(role.getId());
    roleDto.setCreateBy(role.getCreateBy());
    roleDto.setCreateTime(role.getCreateTime());
    roleDto.setModifyBy(role.getModifyBy());
    roleDto.setModifyTime(role.getModifyTime());
    return roleDto;
  }


  /**
   * 转换成实体.
   *
   * @param roleBody roleBody
   * @return 实体
   */
  public Role mapRole(RoleBody roleBody) {
    Role role = new Role();
    role.setName(roleBody.getName());
    role.setDescription(roleBody.getDescription());
    role.setDisabled(roleBody.getDisabled());
    role.setDepartments(roleBody.getDeptIds()
        .stream().map(Dept::new).collect(Collectors.toSet()));
    role.setMenus(roleBody.getMenuIds().stream().map(e -> {
      Menu menu = new Menu();
      menu.setId(e);
      return menu;
    }).collect(Collectors.toSet()));
    role.setId(roleBody.getId());
    return role;
  }
}
