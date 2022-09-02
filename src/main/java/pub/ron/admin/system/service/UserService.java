package pub.ron.admin.system.service;

import java.util.Optional;
import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.body.UserBaseBody;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.ModifyPassDto;

/**
 * user service.
 *
 * @author ron 2020/11/18
 */
public interface UserService extends BaseService<User> {

  Optional<User> findByUsername(String username);

  void modifyPass(ModifyPassDto modifyPassDto);

  void forceModifyPass(String username, String password);

  void modifyUserBase(UserBaseBody userBaseBody);
}
