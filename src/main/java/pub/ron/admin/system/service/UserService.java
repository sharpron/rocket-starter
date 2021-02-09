package pub.ron.admin.system.service;

import pub.ron.admin.common.BaseService;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.ModifyPassDto;

/**
 * @author ron 2020/11/18
 */
public interface UserService extends BaseService<User> {

  void modifyPass(ModifyPassDto modifyPassDto);

  void forceModifyPass(String username, String password);

}