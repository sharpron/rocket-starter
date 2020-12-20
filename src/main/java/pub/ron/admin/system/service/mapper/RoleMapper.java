package pub.ron.admin.system.service.mapper;

import pub.ron.admin.system.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.domain.Role;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

  @DeptMapping
  Role mapRole(RoleBody body);

  @Mapping(target = "deptId", source = "role.dept.id")
  @Mapping(target = "deptName", source = "role.dept.name")
  RoleDto mapDto(Role role);

}
