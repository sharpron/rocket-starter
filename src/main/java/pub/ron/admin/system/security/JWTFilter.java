package pub.ron.admin.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import pub.ron.admin.common.ErrorInfo;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import pub.ron.admin.system.security.provider.TokenException;
import pub.ron.admin.system.security.realm.RefreshTokenException;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a
 * valid user is found.
 */
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends GenericFilterBean {

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String TOKEN_PREFIX = "Bearer ";

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String jwt = resolveToken(httpServletRequest);

    if (StringUtils.hasText(jwt)) {
      try {
        SecurityUtils.getSubject().login(new JwtToken(jwt));
        filterChain.doFilter(servletRequest, servletResponse);
      } catch (RefreshTokenException e) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + e.getToken());
      } catch (AuthenticationException e) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OBJECT_MAPPER.writeValue(
            response.getOutputStream(),
            new ErrorInfo("jwt认证失败")
        );
      }
    }

  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
