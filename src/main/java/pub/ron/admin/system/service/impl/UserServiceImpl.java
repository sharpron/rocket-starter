package pub.ron.admin.system.service.impl;

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import pub.ron.admin.common.AbstractService;
import pub.ron.admin.common.AppException;
import pub.ron.admin.common.BaseRepo;
import pub.ron.admin.system.body.UserBaseBody;
import pub.ron.admin.system.domain.User;
import pub.ron.admin.system.dto.ModifyPassDto;
import pub.ron.admin.system.repo.UserRepo;
import pub.ron.admin.system.security.PasswordEncoder;
import pub.ron.admin.system.security.Principal;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.service.UserService;

/**
 * user service impl.
 *
 * @author ron 2020/11/18
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends AbstractService<User> implements UserService {

  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;


  @Override
  protected BaseRepo<User> getBaseRepo() {
    return userRepo;
  }

  @Override
  protected void beforeCreate(User user) {
    final String salt = randomSalt();
    final String encryptPass = passwordEncoder.encoded(user.getPassword(), salt);
    user.setPasswordSalt(salt);
    user.setPassword(encryptPass);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepo.findByUsername(username);
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void modifyPass(ModifyPassDto modifyPassDto) {
    Long userId = SubjectUtils.currentUser().getUserId();
    User user = userRepo.findById(userId).orElseThrow();

    final String encoded =
        passwordEncoder.encoded(modifyPassDto.getOldPass(), user.getPasswordSalt());
    if (!encoded.equals(user.getPassword())) {
      throw new AppException("修改密码失败:原密码错误");
    }
    this.forceModifyPass(user.getUsername(), modifyPassDto.getNewPass());
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void forceModifyPass(String username, String password) {
    final String salt = randomSalt();
    final String encryptPass = passwordEncoder.encoded(password, salt);
    userRepo.updatePass(username, encryptPass, salt);
  }

  @Override
  public void modifyUserBase(UserBaseBody userBaseBody) {
    Long userId = SubjectUtils.currentUser().getUserId();
    User user = userRepo.findById(userId).orElseThrow();
    user.setNickname(userBaseBody.getNickname());
    user.setEmail(userBaseBody.getEmail());
    user.setMobile(userBaseBody.getMobile());
    userRepo.save(user);
    SubjectUtils.updatePrincipal(SecurityUtils.getSubject(), createPrincipal(user));
  }

  @Override
  protected void afterUpdate(User user) {
    super.afterUpdate(user);
    SubjectUtils.updatePrincipal(SecurityUtils.getSubject(), createPrincipal(user));
  }

  @Override
  protected void beforeDelete(User user) {
    if (User.ADMIN.equals(user.getUsername())) {
      throw new AppException("不能删除超级管理员");
    }
  }

  private static Principal createPrincipal(User user) {
    Principal principal = SubjectUtils.currentUser();
    return Principal.builder()
        .username(user.getUsername())
        .nickname(user.getNickname())
        .mobile(user.getMobile())
        .email(user.getEmail())
        .userId(user.getId())
        .deptId(user.getDept().getId())
        .deptName(user.getDept().getName())
        .deptPath(user.getDept().getPath())
        .manageDeptIds(principal.getManageDeptIds())
        .perms(principal.getPerms())
        .build();
  }

  private static String randomSalt() {
    return UUID.randomUUID().toString();
  }

}
