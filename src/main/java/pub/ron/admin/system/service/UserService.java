package pub.ron.admin.system.service;

import pub.ron.admin.system.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pub.ron.admin.system.body.CreateUserBody;
import pub.ron.admin.system.body.ModifyUserBody;
import pub.ron.admin.system.dto.ModifyPassDto;
import pub.ron.admin.system.dto.UserQuery;

/**
 * @author ron 2020/11/18
 */
public interface UserService {

  void create(CreateUserBody createUserBody);

  void update(ModifyUserBody modifyUserBody);

  void modifyPass(ModifyPassDto modifyPassDto);

  void forceModifyPass(String username, String password);

  void removeById(Long id);

  Page<UserDto> findByPage(Pageable pageable, UserQuery userQuery);
}
