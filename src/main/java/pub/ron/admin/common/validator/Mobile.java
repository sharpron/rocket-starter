package pub.ron.admin.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author ron 2020/11/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotBlank
@Pattern(
    regexp = "^((13[0-9])|(14[014-9])|(15[0-3,5-9])|(16[2567])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\\\d{8}$",
    message = "手机号码格式错误"
)
public @interface Mobile {

}
