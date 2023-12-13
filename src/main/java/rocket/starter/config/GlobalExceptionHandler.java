package rocket.starter.config;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rocket.starter.common.AppException;
import rocket.starter.common.ErrorInfo;

/**
 * Global Exception handlers.
 *
 * @author ron 2020/11/19
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * 自定义应用异常处理.
   *
   * @param e e
   * @return Construct an Error from Exception's message
   */
  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleApp(AppException e) {
    log.error("handle failed!", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorInfo());
  }

  /**
   * validation exception handler.
   *
   * @param e e
   * @return response
   */
  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleConstraint(MethodArgumentNotValidException e) {
    final FieldError fieldError = e.getFieldError();
    Objects.requireNonNull(fieldError);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorInfo(fieldError.getField() + " " + fieldError.getDefaultMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleUnauthorized(UnauthenticatedException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorInfo(e.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleUnauthorized(UnauthorizedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorInfo(e.getMessage()));
  }

  /**
   * 处理未处理的异常.
   *
   * @param e 异常
   * @return 结果
   */
  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleOther(Throwable e) {
    log.error("handle failed!", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorInfo("服务器内部错误"));
  }
}
