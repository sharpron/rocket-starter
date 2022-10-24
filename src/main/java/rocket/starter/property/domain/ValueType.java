package rocket.starter.property.domain;

/**
 * 值类型.
 *
 * @author ron 2021/10/21
 */

public enum ValueType {

  /**
   * 字符串.
   */
  @SuppressWarnings("unused")
  STR("字符串"),

  /**
   * 数字.
   */
  NUMBER("数字");

  private final String desc;


  ValueType(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
