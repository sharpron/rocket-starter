package pub.ron.admin.logging.service;

import pub.ron.admin.logging.domain.Logging;
import pub.ron.admin.logging.domain.Status;
import pub.ron.admin.logging.repository.LoggingRepository;
import pub.ron.admin.logging.util.IpUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * @author ron 2020/9/20
 */
@Service
@RequiredArgsConstructor
public class LoggingService {


  /**
   * 自动注入日志模块
   */
  private final LoggingRepository loggingRepository;

  /**
   * current request
   */
  private final HttpServletRequest request;

  /**
   * 为操作添加日志
   *
   * @param func 操作
   * @param log  日志
   * @return func returns
   * @throws Throwable throws on func call
   */
  public Object addLogForOperation(Func func, String log) throws Throwable {
    long beginTime = System.currentTimeMillis();
    Exception exception = null;
    try {
      return func.call();
    } catch (Exception e) {
      exception = e;
      throw e;
    } finally {
      long time = System.currentTimeMillis() - beginTime;

      Logging logging = new Logging();
      logging.setDescription(log);
      logging.setSpendTime(time);

      String clientIpAddr = IpUtils.getClientIpAddr(request);
      logging.setClientIp(clientIpAddr);
      logging.setClientRegion(IpUtils.ip2Region(clientIpAddr));
      logging.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));

      if (exception == null) {
        logging.setStatus(Status.OK);
      } else {
        logging.setStatus(Status.FAIL);
        String errMsg = LoggingService.getErrMsg(exception);
        logging.setException(errMsg);
      }
      loggingRepository.save(logging);
    }
  }


  /**
   * 获取异常日志
   *
   * @param e 异常
   * @return 日志信息
   */
  private static String getErrMsg(Throwable e) {
    StringWriter stringWriter = new StringWriter();
    try (PrintWriter pw = new PrintWriter(stringWriter)) {
      e.printStackTrace(pw);
      return stringWriter.toString();
    }
  }

  /**
   * 接口回调
   */
  @FunctionalInterface
  public interface Func {

    /**
     * 调用
     *
     * @return 方法调用结果
     * @throws Throwable 异常
     */
    Object call() throws Throwable;
  }
}
