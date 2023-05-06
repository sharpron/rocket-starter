package rocket.starter.common;

import lombok.Getter;

/**
 * 应用异常 所有可处理的异常应该继承它.
 *
 * @author ron 2020/11/19
 */
@Getter
public class AppException extends RuntimeException {

  /**
   * 错误信息.
   */
  private final ErrorInfo errorInfo;

  public AppException(int code, String message, Throwable throwable) {
    super(message, throwable);
    this.errorInfo = new ErrorInfo(code, message);
  }

  public AppException(String message, Throwable throwable) {
    super(message, throwable);
    this.errorInfo = new ErrorInfo(message);
  }

  /**
   * 使用指定的错误码和消息构造一个异常.
   *
   * @param code    错误码
   * @param message 消息
   */
  public AppException(int code, String message) {
    super(message);
    this.errorInfo = new ErrorInfo(code, message);
  }

  /**
   * 使用默认的错误码和消息构建异常.
   *
   * @param message 消息
   */
  public AppException(String message) {
    super(message);
    this.errorInfo = new ErrorInfo(message);
  }
}
