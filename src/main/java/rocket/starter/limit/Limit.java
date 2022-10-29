package rocket.starter.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Current limiting tool.
 *
 * @author herong 2021/2/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Limit {

  /**
   * 设置限流单位时间.
   *
   * @return 单位时间(毫秒)
   */
  int periodMills();

  /**
   * 设置最大访问次数.
   *
   * @return 最大访问次数
   */
  int maxCount();

  /**
   * 限流方式.
   *
   * @return 类型
   */
  Type type() default Type.METHOD_NAME;

  /**
   * 限流方.
   */
  enum Type {
    /**
     * 由服务端决定.
     */
    METHOD_NAME,
    /**
     * 针对客户端.
     */
    IP_ADDRESS
  }
}
