package pub.ron.admin.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author ron 2020/11/19
 */
@Getter
public class AppException extends RuntimeException {

  private final HttpStatus status;

  public AppException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public AppException(String message) {
    this(HttpStatus.BAD_REQUEST, message);
  }
}
