package pub.ron.admin.system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.UserBody;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.UserDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  @DeptMapping
  User mapUser(UserBody body);

  @Mapping(target = "deptId", source = "user.dept.id")
  @Mapping(target = "deptName", source = "user.dept.name")
  UserDto mapUserDto(User user);
}
