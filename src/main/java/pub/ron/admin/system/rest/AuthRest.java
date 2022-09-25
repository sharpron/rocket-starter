package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Base64;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.admin.common.AppException;
import pub.ron.admin.logging.Log;
import pub.ron.admin.system.dto.AuthResultDto;
import pub.ron.admin.system.dto.AuthResultDto.Status;
import pub.ron.admin.system.dto.CaptchaDto;
import pub.ron.admin.system.dto.LoginDto;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.UserLocker;
import pub.ron.admin.system.service.CaptchaService;
import pub.ron.admin.system.service.CaptchaService.Captcha;
import pub.ron.admin.system.service.UserService;

/**
 * auth rest provider.
 *
 * @author ron 2020/11/19
 */
@Slf4j
@RestController
@RequestMapping("/api")
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
  @Operation(tags = "用户登录")
  @PostMapping("/authenticate")
  @Log("用户登录")
  public ResponseEntity<AuthResultDto> authenticate(
      @Valid @RequestBody LoginDto loginDto) {

    captchaService.check(loginDto.getCaptchaKey(), loginDto.getCaptcha());
    final Subject subject = SecurityUtils.getSubject();

    try {
      subject.login(new UsernamePasswordToken(loginDto.getUsername(), loginDto.getPassword()));
    } catch (UnknownAccountException | IncorrectCredentialsException e) {
      if (userLocker.checkLockedWhenFail(loginDto.getUsername())) {
        throw new AppException("登录失败次数过多，请稍候再试");
      } else {
        throw new AppException("用户名或密码错误");
      }
    } catch (LockedAccountException e) {
      throw new AppException("登录失败次数过多，请稍候再试");
    } catch (ExpiredCredentialsException e) {
      if (StringUtils.isBlank(loginDto.getNewPassword())) {
        // 通知客户端需要携带新密码进行修改
        return ResponseEntity.ok(AuthResultDto.other(Status.PASSWORD_EXPIRE));
      }
      // 修改密码
      Long userId = Long.valueOf(e.getMessage());
      userService.forceModifyPass(userId, loginDto.getNewPassword());
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
  @Operation(tags = "查询当前的登录身份")
  @RequiresAuthentication
  public ResponseEntity<?> getUserInfo() {
    return ResponseEntity.ok(SubjectUtils.currentUser());
  }

  /**
   * get captcha code.
   *
   * @return response
   */
  @Operation(tags = "获取验证码")
  @GetMapping("/captcha")
  public ResponseEntity<CaptchaDto> getCaptcha() {
    final Captcha captcha = captchaService.genCaptcha();

    String base64Data = Base64.getEncoder()
        .encodeToString(captcha.getBytes());

    return ResponseEntity.ok(new CaptchaDto(
        captcha.getKey(), "data:image/jpeg;base64," + base64Data
    ));
  }

  @Operation(tags = "退出登录")
  @DeleteMapping("/tokens")
  @Log("退出登录")
  public ResponseEntity<?> logout() {
    SecurityUtils.getSubject().logout();
    return ResponseEntity.noContent().build();
  }

}
