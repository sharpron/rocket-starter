package pub.ron.admin.system.security;

import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ron 2020/12/17
 */
@RequiredArgsConstructor
public class JwtToken implements AuthenticationToken {

  private final String token;

  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return token;
  }
}
