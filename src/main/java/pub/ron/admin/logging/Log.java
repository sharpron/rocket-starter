package pub.ron.admin.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法上进行日志申明
 *
 * @author ron 2020.09.19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

  /**
   * @return 日志申明名称
   */
  String value() default "";

}
