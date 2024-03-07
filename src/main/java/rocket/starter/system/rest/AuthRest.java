package rocket.starter.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocket.starter.common.AppException;
import rocket.starter.logging.Log;
import rocket.starter.system.dto.AuthResultDto;
import rocket.starter.system.dto.AuthResultDto.Status;
import rocket.starter.system.dto.CaptchaDto;
import rocket.starter.system.dto.LoginDto;
import rocket.starter.system.security.Principal;
import rocket.starter.system.security.SubjectUtils;
import rocket.starter.system.security.UserLocker;
import rocket.starter.system.service.CaptchaService;
import rocket.starter.system.service.UserService;

/**
 * auth rest provider.
 *
 * @author ron 2020/11/19
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "认证管理")
@RequiredArgsConstructor
public class AuthRest {

  private final CaptchaService captchaService;
  private final UserLocker userLocker;

  private final UserService userService;

  /**
   * 用户登录.
   *
   * @param loginDto loginDto
   * @return response
   */
  @Operation(summary = "用户登录")
  @PostMapping("/authenticate")
  @Log("用户登录")
  public ResponseEntity<AuthResultDto> authenticate(
      @Validated @RequestBody LoginDto loginDto) {

    captchaService.check(loginDto.getCaptchaKey(), loginDto.getCaptcha());
    final Subject subject = SecurityUtils.getSubject();

    try {
      subject.login(new UsernamePasswordToken(loginDto.getUsername(), loginDto.getPassword()));
    } catch (UnknownAccountException | IncorrectCredentialsException e) {
      if (userLocker.tryLocked(loginDto.getUsername())) {
        throw new AppException("登录失败次数过多，请稍候再试");
      } else {
        throw new AppException("用户名或密码错误");
      }
    } catch (LockedAccountException e) {
      throw new AppException("登录失败次数过多，请稍候再试");
    }

    Principal principal = (Principal) subject.getPrincipal();

    // 检查密码是否过期
    if (userService.isPassExpired(principal.getPasswordExpireAt())) {
      if (StringUtils.isBlank(loginDto.getNewPassword())) {
        // 通知客户端需要携带新密码进行修改
        return ResponseEntity.ok(AuthResultDto.other(Status.PASSWORD_EXPIRE));
      }

      userService.forceModifyPass(principal.getUserId(), loginDto.getNewPassword());
      // 通知前端使用新密码重新登录
      return ResponseEntity.ok(AuthResultDto.other(Status.MODIFY_EXPIRE_SUCCESS));
    }

    return ResponseEntity.ok(AuthResultDto.ok(SubjectUtils.currentUser()));
  }

  @GetMapping("is-authenticated")
  public ResponseEntity<?> isAuthenticated() {
    boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
    return ResponseEntity.ok(authenticated);
  }

  /**
   * 查询当前的登录身份.
   *
   * @return response
   */
  @GetMapping("authenticated")
  @Operation(summary = "查询当前的登录身份")
  @RequiresAuthentication
  public ResponseEntity<?> getUserInfo() {
    return ResponseEntity.ok(SubjectUtils.currentUser());
  }

  /**
   * get captcha code.
   *
   * @return response
   */
  @Operation(summary = "获取验证码")
  @GetMapping("/captcha")
  public ResponseEntity<CaptchaDto> getCaptcha() {
    final CaptchaService.Captcha captcha = captchaService.genCaptcha();

    String base64Data = Base64.getEncoder()
        .encodeToString(captcha.getBytes());

    return ResponseEntity.ok(new CaptchaDto(
        captcha.getKey(), "data:image/jpeg;base64," + base64Data
    ));
  }

  @Operation(summary = "退出登录")
  @DeleteMapping("/tokens")
  @Log("退出登录")
  public ResponseEntity<?> logout() {
    SecurityUtils.getSubject().logout();
    return ResponseEntity.noContent().build();
  }

}
