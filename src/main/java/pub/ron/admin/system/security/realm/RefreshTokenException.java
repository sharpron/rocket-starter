package pub.ron.admin.system.security.realm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;

/**
 * refresh token exception.
 *
 * @author herong 2021/5/30
 */
@RequiredArgsConstructor
@Getter
public class RefreshTokenException extends AuthenticationException {

  private final String token;
}
