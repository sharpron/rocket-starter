package pub.ron.admin.system.service.mapper;

import pub.ron.admin.system.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pub.ron.admin.system.body.CreateUserBody;
import pub.ron.admin.system.body.ModifyUserBody;
import pub.ron.admin.system.domain.User;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

  @DeptMapping
  @Mapping(target = "password", ignore = true)
  User mapUser(CreateUserBody body);

  @DeptMapping
  User mapUser(ModifyUserBody body);

  @Mapping(target = "deptId", source = "user.dept.id")
  @Mapping(target = "deptName", source = "user.dept.name")
  UserDto mapUserDto(User user);
}
