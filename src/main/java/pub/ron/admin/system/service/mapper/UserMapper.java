package pub.ron.admin.system.service.mapper;

import org.springframework.stereotype.Component;
import pub.ron.admin.system.body.UserBody;
import pub.ron.admin.system.domain.Dept;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.UserDto;

/**
 * user mapper.
 *
 * @author ron 2022/8/6
 */
@Component
public class UserMapper {

  /**
   * 转换到实体.
   *
   * @param body body
   * @return 实体
   */
  public User mapUser(UserBody body) {
    User user = new User();
    user.setUsername(body.getUsername());
    user.setNickname(body.getNickname());
    user.setPassword(body.getPassword());
    user.setMobile(body.getMobile());
    user.setEmail(body.getEmail());
    user.setDisabled(body.getDisabled());
    user.setDept(body.getDeptId() == null ? null : new Dept(body.getDeptId()));
    user.setRoleIds(body.getRoleIds());
    user.setId(body.getId());
    return user;
  }

  /**
   * 转换到dto.
   *
   * @param user user
   * @return dto
   */
  public UserDto mapUserDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setDeptId(user.getDept().getId());
    userDto.setDeptName(user.getDept().getName());
    userDto.setUsername(user.getUsername());
    userDto.setNickname(user.getNickname());
    userDto.setMobile(user.getMobile());
    userDto.setEmail(user.getEmail());
    userDto.setDisabled(user.getDisabled());
    userDto.setId(user.getId());
    userDto.setCreateBy(user.getCreateBy());
    userDto.setCreateTime(user.getCreateTime());
    userDto.setModifyBy(user.getModifyBy());
    userDto.setModifyTime(user.getModifyTime());
    return userDto;
  }
}
