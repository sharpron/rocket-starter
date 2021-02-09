package pub.ron.admin.system.rest;

import pub.ron.admin.common.AppException;
import pub.ron.admin.system.dto.LoginDto;
import pub.ron.admin.system.security.JWTFilter;
import pub.ron.admin.system.security.TokenProvider;
import pub.ron.admin.system.security.principal.UserPrincipal;
import pub.ron.admin.system.service.CaptchaService;
import pub.ron.admin.system.service.CaptchaService.Captcha;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ron 2020/11/19
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthRest {

  private static final String CACHE_KEY = "Cache-Key";

  private final TokenProvider tokenProvider;

  private final CaptchaService captchaService;


  @Operation(tags = "用户登录")
  @PostMapping("/authenticate")
  public ResponseEntity<JWTToken> authenticate(
      @Valid @RequestBody LoginDto loginDto,
      @RequestHeader(CACHE_KEY) String cacheKey) {

    captchaService.check(cacheKey, loginDto.getCaptcha());
    final Subject subject = SecurityUtils.getSubject();

    try {
      subject.login(
          new UsernamePasswordToken(loginDto.getUsername(), loginDto.getPassword())
      );
    } catch (UnknownAccountException | IncorrectCredentialsException e) {
      throw new AppException("用户名或密码错误");
    }

    final String token = tokenProvider.generateToken(
        (UserPrincipal) subject.getPrincipal(),
        loginDto.isRememberMe()
    );
    return ResponseEntity
        .ok()
        .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.TOKEN_PREFIX + token)
        .body(new JWTToken(token));
  }

  @Operation(tags = "获取验证码")
  @GetMapping("/captcha")
  public ResponseEntity<byte[]> getCaptcha() {
    final Captcha captcha = captchaService.genCaptcha();
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noCache())
        .contentType(MediaType.IMAGE_PNG)
        .header(CACHE_KEY, captcha.getKey())
        .body(captcha.getBytes());
  }

  @Operation(tags = "退出登录")
  @GetMapping("/logout")
  public ResponseEntity<?> logout(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    tokenProvider.clear(token);
    return ResponseEntity.noContent().build();
  }


  /**
   * Object to return as body in JWT Authentication.
   */
  @Value
  static class JWTToken {

    String token;
  }
}
