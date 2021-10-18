package pub.ron.admin.system.service.impl;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.AppException;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.ModifyPassDto;
import pub.ron.admin.system.repo.UserRepo;
import pub.ron.admin.system.security.PasswordEncoder;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.UserService;

/**
 * user service impl.
 *
 * @author ron 2020/11/18
 */
@Service
@Slf4j
public class UserServiceImpl extends AbstractService<User, UserRepo> implements UserService {

  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepo repository, PasswordEncoder passwordEncoder) {
    super(repository);
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void create(User user) {
    final String salt = randomSalt();
    final String encryptPass = passwordEncoder.encoded(user.getPassword(), salt);
    user.setPasswordSalt(salt);
    user.setPassword(encryptPass);
    super.create(user);
  }

  @Override
  public void modifyPass(ModifyPassDto modifyPassDto) {
    final String username = SubjectUtils.getCurrentUsername().orElseThrow();
    User user = repository.findByUsername(username).orElseThrow(() -> new AppException("用户状态异常"));

    final String encoded =
        passwordEncoder.encoded(modifyPassDto.getOldPass(), user.getPasswordSalt());
    if (encoded.equals(user.getPassword())) {
      this.forceModifyPass(username, modifyPassDto.getNewPass());
    }
    throw new AppException("修改密码失败:原密码错误");
  }

  @Override
  public void forceModifyPass(String username, String password) {
    final String salt = randomSalt();
    final String encryptPass = passwordEncoder.encoded(password, salt);
    repository.updatePass(username, encryptPass, salt);
  }

  @Override
  public void deleteById(Long id) {
    final Optional<User> optionalUser = repository.findById(id);
    optionalUser.ifPresent(
        user -> {
          if (User.ADMIN.equals(user.getUsername())) {
            throw new AppException("不能删除超级管理员");
          }
          repository.deleteById(id);
        });
  }

  private static String randomSalt() {
    return UUID.randomUUID().toString();
  }

}
