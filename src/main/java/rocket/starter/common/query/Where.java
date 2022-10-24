package rocket.starter.common.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定条件.
 *
 * @author ron 2020/11/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Where {

  /**
   * criteria root.
   *
   * @return root name
   */
  String root() default "";

  /**
   * 全等比较.
   *
   * @return relation query type
   */
  Type type() default Type.eq;

  /**
   * relation query type.
   */
  enum Type {
    /**
     * full like. '%example%'
     */
    like,
    /**
     * right like. 'example %'
     */
    right_like,
    /**
     * equals.
     */
    eq,
    /**
     * less than.
     */
    lt,
    /**
     * greater than.
     */
    gt,
    /**
     * less than or equals.
     */
    le,
    /**
     * greater than or equals.
     */
    ge,
    /**
     * less than or equals bigger greater than or equals smaller.
     *
     * <p>List&lt;Long&gt; size=2
     */
    between,
    /**
     * only time, Epoch milliseconds.
     *
     * <p>List&lt;Long&gt; size=2
     */
    betweenTime,

    in,
  }
}
