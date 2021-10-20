package pub.ron.admin.logging;

import org.springframework.stereotype.Component;
import pub.ron.admin.common.AppException;

/**
 * 日志测试用例.
 *
 * @author ron 2021/10/20
 */
@Component
public class LogExample {

  @Log("拜访日志")
  public void causeVisitLog(String info) {

  }

  @Log("错误日志")
  public void causeErrorLog() {
    throw new AppException("发生了错误");
  }
}
