package pub.ron.admin.common.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ron 2020/11/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Where {

  String root() default "";

  Type type();

  enum Type {
    like,
    eq,
    lt,
    gt,
    le,
    ge,
    between,
    betweenTime
  }
}
