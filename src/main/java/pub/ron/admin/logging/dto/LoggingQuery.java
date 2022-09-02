package pub.ron.admin.logging.dto;

import java.util.List;
import lombok.Data;
import pub.ron.admin.common.query.Where;
import pub.ron.admin.common.query.Where.Type;
import pub.ron.admin.logging.domain.Status;

/**
 * query for logging.
 *
 * @author ron 2021/1/1
 */
@Data
public class LoggingQuery {

  @Where
  private String createBy;

  /**
   * 描述.
   */
  @Where(type = Type.like)
  private String description;

  /**
   * 花费时间.
   */
  @Where(type = Type.between)
  private List<Long> spendTime;

  /**
   * 状态 成功或者失败.
   */
  @Where
  private Status status;

  /**
   * 异常发生时的异常信息.
   */
  @Where(type = Type.like)
  private String exception;

  /**
   * 客户端IP.
   */
  @Where(type = Type.like)
  private String clientIp;

  /**
   * 客户端区域.
   */
  @Where(type = Type.like)
  private String clientRegion;

  /**
   * 用户代理，浏览器信息等.
   */
  @Where(type = Type.like)
  private String userAgent;
}
