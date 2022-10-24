package rocket.starter.logging.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import rocket.starter.common.AbstractService;
import rocket.starter.common.BaseRepo;
import rocket.starter.logging.domain.Logging;
import rocket.starter.logging.domain.Status;
import rocket.starter.logging.repository.LoggingRepository;
import rocket.starter.logging.util.IpUtils;

/**
 * logging service.
 *
 * @author ron 2020/9/20
 */
@Service
@RequiredArgsConstructor
public class LoggingService extends AbstractService<Logging> {

  /**
   * 自动注入日志模块.
   */
  private final LoggingRepository loggingRepository;

  /**
   * current request proxy.
   */
  private final HttpServletRequest request;

  /**
   * 为操作添加日志.
   *
   * @param func   操作
   * @param log    日志
   * @param params 参数
   * @return func returns
   * @throws Throwable throws on func call
   */
  public Object addLogForOperation(Func func, String log, String params) throws Throwable {
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
      logging.setParams(params);
      logging.setDescription(log);
      logging.setSpendTime(time);

      String clientIp = IpUtils.getClientIpAddress(request);
      logging.setClientIp(clientIp);
      logging.setClientRegion(IpUtils.ip2Region(clientIp));
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
   * 获取异常日志.
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
   * 通过状态删除日志.
   *
   * @param status 状态
   */
  @Transactional
  public void clear(Status status) {
    loggingRepository.deleteByStatus(status);
  }

  @Override
  protected BaseRepo<Logging> getBaseRepo() {
    return loggingRepository;
  }

  /**
   * 接口回调.
   */
  @FunctionalInterface
  public interface Func {

    /**
     * 调用.
     *
     * @return 方法调用结果
     * @throws Throwable 异常
     */
    Object call() throws Throwable;
  }
}
