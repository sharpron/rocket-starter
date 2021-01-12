package pub.ron.admin.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务出现异常
 *
 * @author ron 2020/11/19
 */
@Getter
public class AppException extends RuntimeException {

  /**
   * 状态码
   */
  private final HttpStatus status;

  /**
   * 使用指定的状态码和消息构造一个异常
   *
   * @param status  状态
   * @param message 消息
   */
  public AppException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  /**
   * 使用默认的状态码和消息构建异常
   *
   * @param message 消息
   */
  public AppException(String message) {
    this(HttpStatus.BAD_REQUEST, message);
  }
}
