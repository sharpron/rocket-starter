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
    regexp = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z0-9\\S]{8,}$",
    message = "字母数字及特殊字符，且以字母开头，8位以上[二级等保要求]"
)
public @interface Password {

}
