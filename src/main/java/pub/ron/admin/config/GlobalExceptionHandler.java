package pub.ron.admin.config;

import pub.ron.admin.common.AppException;
import pub.ron.admin.common.ErrorInfo;
import java.util.Objects;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Global Exception handlers.
 *
 * @author ron 2020/11/19
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Customize Application Exception
   *
   * @param e e
   * @return Construct a Error from Exception's message
   */
  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleApp(AppException e) {
    log.error("handle failed!", e);
    return ResponseEntity.status(e.getStatus())
        .body(new ErrorInfo(e.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleConstraint(MethodArgumentNotValidException e) {
    final FieldError fieldError = e.getFieldError();
    Objects.requireNonNull(fieldError);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorInfo(
            fieldError.getField() + fieldError.getDefaultMessage()
        ));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleConstraint(ConstraintViolationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(e.getConstraintViolations()
            .stream().findFirst()
            .map(violation -> new ErrorInfo(violation.getPropertyPath() + violation.getMessage()))
            .orElseThrow(AssertionError::new));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleUnauthorized(UnauthorizedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorInfo(e.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorInfo> handleOther(Exception e) {
    log.error("handle failed!", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorInfo("服务器内部错误"));
  }
}
