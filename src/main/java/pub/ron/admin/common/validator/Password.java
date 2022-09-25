package pub.ron.admin.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * 密码验证格式.
 *
 * @author ron 2020/11/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
@Pattern(
    regexp = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)"
        + "(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,30}$",
    message = "必须有字母数字特殊符号, 且长度为[8, 30]"
)
public @interface Password {

  /**
   * the error message template.
   */
  String message() default "必须有字母数字特殊符号, 且长度为[8, 30]";

  /**
   * groups.
   *
   * @return groups
   */
  Class<?>[] groups() default {};

  /**
   * payload.
   *
   * @return payloads
   */
  Class<? extends Payload>[] payload() default {};
}
