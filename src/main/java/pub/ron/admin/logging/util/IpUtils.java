package pub.ron.admin.logging.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * ip地址工具.
 *
 * @author ron 2020/9/20
 */
@Slf4j
public class IpUtils {

  private static final byte[] IP_2_REGION;

  static {
    try (InputStream resourceAsStream = IpUtils.class
        .getResourceAsStream("/plugin/ip2region.xdb")) {
      if (resourceAsStream == null) {
        throw new RuntimeException("/plugin/ip2region.xdb 不存在");
      }
      IP_2_REGION = resourceAsStream.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final ThreadLocal<Searcher> THREAD_LOCAL = ThreadLocal.withInitial(() -> {
    try {
      return Searcher.newWithBuffer(IP_2_REGION);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  });

  /**
   * 常见的包含ip的http header.
   */
  private static final String[] CONTAINS_IP_HEADERS = {
      "X-Forwarded-For",
      "Proxy-Client-IP",
      "WL-Proxy-Client-IP",
      "HTTP_CLIENT_IP",
      "HTTP_X_FORWARDED_FOR"};

  /**
   * 禁用构造器.
   */
  private IpUtils() {
  }

  /**
   * 获取客户端ip地址.
   *
   * @param request 请求
   * @return ip地址
   */
  public static String getClientIpAddress(HttpServletRequest request) {

    String ip = null;
    for (String ipHeader : CONTAINS_IP_HEADERS) {
      ip = request.getHeader(ipHeader);
      if (!isInvalid(ip)) {
        break;
      }
    }

    if (isInvalid(ip)) {
      ip = request.getRemoteAddr();
    }

    if ("127.0.0.1".equals(ip)) {
      try {
        return InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
        return null;
      }
    }
    return ip;
  }

  /**
   * 是否是非法的IP地址.
   *
   * @param ip IP地址
   * @return 如果是非法的地址返回true, 否则返回false
   */
  private static boolean isInvalid(String ip) {
    return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
  }

  /**
   * 根据ip地址获得ip地址所在位置.
   *
   * @param ip ip地址
   * @return 所在位置
   */
  public static String ip2Region(String ip) {
    try {
      return THREAD_LOCAL.get().search(ip);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
