package pub.ron.admin.system.service.impl;

import pub.ron.admin.common.Pages;
import pub.ron.admin.common.query.WhereBuilder;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.domain.RoleMenu;
import pub.ron.admin.system.dto.RoleDto;
import pub.ron.admin.system.dto.RoleQuery;
import pub.ron.admin.system.dto.SimpleRoleDto;
import pub.ron.admin.system.repo.RoleMenuRepo;
import pub.ron.admin.system.repo.RoleRepo;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.service.RoleService;
import pub.ron.admin.system.service.mapper.RoleMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ron 2020/11/22
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepo roleRepo;

  private final RoleMapper roleMapper;

  private final RoleMenuRepo roleMenuRepo;


  @Override
  public void create(RoleBody roleBody) {
    final Role role = roleMapper.mapRole(roleBody);
    roleRepo.save(role);
    updateRoleMenus(role.getId(), roleBody);
  }

  /**
   * 更新角色权限
   *
   * @param roleId   角色id
   * @param roleBody 角色相关数据
   */
  private void updateRoleMenus(Long roleId, RoleBody roleBody) {
    final List<Long> menus = roleBody.getMenus();
    if (menus == null || menus.isEmpty()) {
      return;
    }

    final List<RoleMenu> roleMenus = menus.stream().map(menuId -> {
      final RoleMenu roleMenu = new RoleMenu();
      roleMenu.setMenuId(menuId);
      roleMenu.setRoleId(roleId);
      return roleMenu;
    }).collect(Collectors.toList());
    roleMenuRepo.saveAll(roleMenus);
  }

  @Override
  public void update(Long id, RoleBody roleBody) {
    final Role role = roleMapper.mapRole(roleBody);
    role.setId(id);
    roleRepo.save(role);

    roleMenuRepo.deleteAllByRoleId(id);
    updateRoleMenus(id, roleBody);
  }

  @Override
  public Page<RoleDto> findByPage(Pageable pageable, RoleQuery roleQuery) {
    final Page<Role> page = roleRepo.findAll(
        WhereBuilder.buildSpecWithDept(roleQuery), pageable);
    return Pages.map(page, roleMapper::mapDto);
  }

  @Override
  public List<SimpleRoleDto> findAll() {
    return roleRepo.findAll(
        WhereBuilder.buildSpecWithDept(null)).stream()
        .map(e -> new SimpleRoleDto(e.getId(), e.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public void remove(Long id) {
    final UserPrincipal userPrincipal = SubjectUtils.currentUser();
    roleRepo.deleteByIdAndDept_IdIn(id, userPrincipal.getDeptIds());
  }
}
