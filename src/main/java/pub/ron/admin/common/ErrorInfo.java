package pub.ron.admin.common;

import lombok.Value;

/**
 * 错误信息的响应类型.
 *
 * @author ron 2020/12/19
 */
@Value
public class ErrorInfo {

  /**
   * 默认错误编码.
   */
  private static final int DEFAULT_CODE = -1;

  /**
   * 自定义错误编码.
   */
  int code;

  /**
   * 自定义错误消息.
   */
  String message;

  public ErrorInfo(String message) {
    this(DEFAULT_CODE, message);
  }

  public ErrorInfo(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
