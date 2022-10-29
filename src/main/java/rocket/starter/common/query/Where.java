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
   * 是否忽略空值，如果是则不处理空值，否则使用添加“is null”作为条件.
   *
   * @return 是否忽略空值
   */
  boolean ignoreNull() default true;

  /**
   * relation query type.
   */
  enum Type {
    /**
     * full like. '%example%'
     */
    like,
    /**
     * right like. 'example%'
     */
    right_like,

    /**
     * left like. '%example'
     */
    left_like,
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
     * Closed interval, 包含开始位置和结束位置.
     *
     * <p>List&lt;Long&gt; size=2
     */
    between,
    /**
     * only time, Epoch milliseconds. 包含开始位置和结束位置.
     *
     * <p>List&lt;Long&gt; size=2
     */
    betweenTime,

    /**
     * In equivalent to SQL.
     */
    in,
  }
}
