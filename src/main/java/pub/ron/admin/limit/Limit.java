package pub.ron.admin.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author herong 2021/2/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Limit {

  /**
   * @return 单位时间(毫秒)
   */
  int periodMills();

  /**
   * @return 最大访问次数
   */
  int maxCount();

  /**
   * @return 类型
   */
  Type type() default Type.METHOD_NAME;

  /**
   * 限流方式
   */
  enum Type {
    /**
     * 由服务端决定
     */
    METHOD_NAME,
    /**
     * 针对客户端
     */
    IP_ADDRESS
  }
}
