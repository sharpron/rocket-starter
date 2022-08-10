package pub.ron.admin.system.service.mapper;

import org.springframework.stereotype.Component;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.dto.RoleDto;

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
    roleDto.setDeptIds(role.getDeptIds());
    roleDto.setMenuIds(role.getMenuIds());
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
    role.setDeptIds(roleBody.getDeptIds());
    role.setMenuIds(roleBody.getMenuIds());
    role.setId(roleBody.getId());
    return role;
  }
}
