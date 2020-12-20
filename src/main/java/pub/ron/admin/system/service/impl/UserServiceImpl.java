package pub.ron.admin.system.service.impl;

import pub.ron.admin.common.AppException;
import pub.ron.admin.common.Pages;
import pub.ron.admin.common.query.WhereBuilder;
import pub.ron.admin.system.body.CreateUserBody;
import pub.ron.admin.system.body.ModifyUserBody;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.ModifyPassDto;
import pub.ron.admin.system.dto.UserDto;
import pub.ron.admin.system.dto.UserQuery;
import pub.ron.admin.system.repo.UserRepo;
import pub.ron.admin.system.security.PasswordEncoder;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.UserService;
import pub.ron.admin.system.service.mapper.UserMapper;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ron 2020/11/18
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepo userRepo;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Override
  @Transactional
  public void create(CreateUserBody createUserBody) {

    User user = userMapper.mapUser(createUserBody);

    log.debug("mapped result {}", user);
    final String salt = randomSalt();
    final String encryptPass = passwordEncoder.encoded(
        createUserBody.getPassword(), salt
    );
    user.setPasswordSalt(salt);

    log.debug("encrypt pass: {}, origin pass: {}", encryptPass, createUserBody.getPassword());
    user.setPassword(encryptPass);

    userRepo.save(user);
    if (createUserBody.getRoleIds() != null) {
      for (Long roleId : createUserBody.getRoleIds()) {
        userRepo.addRoleRelations(user.getId(), roleId);
      }
    }

  }

  @Override
  @Transactional
  public void update(ModifyUserBody modifyUserBody) {
    User user = userMapper.mapUser(modifyUserBody);
    log.debug("mapped result {}", user);

    userRepo.update(user);
    userRepo.clearRoles(modifyUserBody.getId());
    for (Long roleId : modifyUserBody.getRoleIds()) {
      userRepo.addRoleRelations(user.getId(), roleId);
    }
  }

  @Override
  public void modifyPass(ModifyPassDto modifyPassDto) {
    final String username = SubjectUtils.getCurrentUsername().orElseThrow();
    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new AppException("用户状态异常"));

    final String encoded = passwordEncoder.encoded(
        modifyPassDto.getOldPass(),
        user.getPasswordSalt()
    );
    if (encoded.equals(user.getPassword())) {
      this.forceModifyPass(username, modifyPassDto.getNewPass());
    }
    throw new AppException("修改密码失败:原密码错误");
  }

  @Override
  public void forceModifyPass(String username, String password) {
    final String salt = randomSalt();
    final String encryptPass = passwordEncoder.encoded(password, salt);
    userRepo.updatePass(username, encryptPass, salt);
  }

  @Override
  public void removeById(Long id) {
    final Optional<User> user = userRepo.findById(id);
    if (user.isEmpty()) {
      return;
    }
    if (user.get().getUsername().equals(User.ADMIN)) {
      throw new AppException("不能删除超级管理员");
    }
    userRepo.deleteById(id);
  }

  @Override
  public Page<UserDto> findByPage(Pageable pageable, UserQuery userQuery) {
    return Pages.map(
        userRepo.findAll(
            WhereBuilder.buildSpec(userQuery),
            pageable
        ),
        userMapper::mapUserDto
    );
  }

  private static String randomSalt() {
    return UUID.randomUUID().toString();
  }

}
