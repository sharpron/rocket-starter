package pub.ron.admin.system.rest;

import io.swagger.v3.oas.annotations.Operation;
import java.time.Duration;
import java.util.Base64;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
import pub.ron.admin.system.dto.CaptchaDto;
import pub.ron.admin.system.dto.LoginDto;
import pub.ron.admin.system.security.SubjectUtils;
import pub.ron.admin.system.security.UserLocker;
import pub.ron.admin.system.service.CaptchaService;
import pub.ron.admin.system.service.CaptchaService.Captcha;

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

  /**
   * 用户登录.
   *
   * @param loginDto loginDto
   * @return response
   */
  @Operation(tags = "用户登录")
  @PostMapping("/authenticate")
  @Log("用户登录")
  public ResponseEntity<?> authenticate(
      @Valid @RequestBody LoginDto loginDto) {

    captchaService.check(loginDto.getCaptchaKey(), loginDto.getCaptcha());
    final Subject subject = SecurityUtils.getSubject();

    try {
      subject.login(new UsernamePasswordToken(loginDto.getUsername(), loginDto.getPassword()));
    } catch (UnknownAccountException | IncorrectCredentialsException e) {
      if (userLocker.checkLockedWhenFail(loginDto.getUsername())) {
        throw new AppException(String.format("登录失败已经超过%d次，账户已被锁定", userLocker.getMaxTryTimes()));
      } else {
        throw new AppException("用户名或密码错误");
      }
    } catch (LockedAccountException e) {
      Duration lockDuration = userLocker.getLockDuration();
      String duration = lockDuration.toString().replace("PT", "")
          .replace("H", "小时")
          .replace("M", "分钟")
          .replace("S", "秒");
      throw new AppException(String.format("账户已被锁定, 请%s后再试", duration));
    }
    return ResponseEntity.ok(SubjectUtils.currentUser());
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
