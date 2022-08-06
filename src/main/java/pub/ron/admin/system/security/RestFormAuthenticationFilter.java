package pub.ron.admin.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 处理失败响应消息.
 *
 * @author ron 2022/7/21
 */
public class RestFormAuthenticationFilter extends FormAuthenticationFilter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    if (isLoginRequest(request, response)) {
      if (isLoginSubmission(request, response)) {
        return executeLogin(request, response);
      } else {
        return true;
      }
    } else {
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      OBJECT_MAPPER.writeValue(httpServletResponse.getOutputStream(), Map.of("message", "会话超时"));
      return false;
    }
  }
}
