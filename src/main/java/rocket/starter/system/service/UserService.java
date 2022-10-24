package rocket.starter.system.service;

import java.util.Optional;
import rocket.starter.common.BaseService;
import rocket.starter.system.body.UserBaseBody;
import rocket.starter.system.domain.User;
import rocket.starter.system.dto.ModifyPassDto;

/**
 * user service.
 *
 * @author ron 2020/11/18
 */
public interface UserService extends BaseService<User> {

  Optional<User> findByUsername(String username);

  void modifyPass(ModifyPassDto modifyPassDto);

  void forceModifyPass(Long userId, String password);

  void modifyUserBase(UserBaseBody userBaseBody);
}
