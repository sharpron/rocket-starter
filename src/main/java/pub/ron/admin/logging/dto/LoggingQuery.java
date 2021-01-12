package pub.ron.admin.logging.dto;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;
import pub.ron.admin.logging.domain.Status;

/**
 * @author ron 2021/1/1
 */
@Data
public class LoggingQuery {

  /**
   * 描述
   */
  @Where(type = Type.like)
  private String description;

  /**
   * 花费时间
   */
  @Where(type = Type.between)
  private List<Long> spendTime;

  /**
   * 状态 成功或者失败
   */
  @Enumerated
  private Status status;

  /**
   * 异常发生时的异常信息
   */
  @Column(columnDefinition = "text")
  private String exception;

  /**
   * 客户端IP
   */
  private String clientIp;

  /**
   * 客户端区域
   */
  private String clientRegion;

  /**
   * 用户代理，浏览器信息等
   */
  private String userAgent;
}
