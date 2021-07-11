package pub.ron.admin.system.security.provider;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pub.ron.admin.common.AppException;
import pub.ron.admin.system.security.principal.UserPrincipal;

/**
 * token exception.
 *
 * @author herong 2021/5/30
 */
@Getter
public class TokenException extends AppException {

  private final Result result;

  private final UserPrincipal userPrincipal;

  /**
   * constructor.
   *
   * @param result result
   * @param message message
   * @param userPrincipal userPrincipal
   */
  public TokenException(Result result, String message, UserPrincipal userPrincipal) {
    super(HttpStatus.UNAUTHORIZED, message);
    this.result = result;
    this.userPrincipal = userPrincipal;
  }

  public enum Result {
    EXPIRED,
    ILLEGAL;
  }
}
