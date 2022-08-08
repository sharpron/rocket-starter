package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.RoleBody;
import pub.ron.admin.system.domain.Role;
import pub.ron.admin.system.dto.RoleDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {


  RoleDto mapDto(Role role);

  Role mapRole(RoleBody roleBody);
}
