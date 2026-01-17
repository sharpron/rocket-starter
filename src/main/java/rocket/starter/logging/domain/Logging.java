package rocket.starter.logging.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rocket.starter.common.BaseEntity;

/**
 * 日志存储.
 *
 * @author ron 2020/9/19
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "logging_log")
public class Logging extends BaseEntity {

  /**
   * 描述.
   */
  private String description;

  /**
   * 执行参数.
   */
  @Column(columnDefinition = "text")
  private String params;

  /**
   * 花费时间.
   */
  private Long spendTime;

  /**
   * 状态 成功或者失败.
   */
  @Enumerated
  private Status status;

  /**
   * 异常发生时的异常信息.
   */
  @Column(columnDefinition = "text")
  private String exception;

  /**
   * 客户端IP.
   */
  private String clientIp;

  /**
   * 客户端区域.
   */
  private String clientRegion;

  /**
   * 用户代理，浏览器信息等.
   */
  private String userAgent;
}
