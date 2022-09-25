package pub.ron.admin.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * 手机号验证格式.
 *
 * @author ron 2020/11/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
@Pattern(regexp =
    "^((13\\d)|(14[014-9])|(15[0-3,5-9])|(16[2567])|(17[0-8])|(18\\d)|(19[0-3,5-9]))\\\\d{8}$",
    message = "手机号格式错误"
)
public @interface Mobile {

  /**
   * the error message template.
   */
  String message() default "手机号格式错误";

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
