package pub.ron.admin.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import pub.ron.admin.system.security.SubjectUtils;

/**
 * @author herong
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

  public static final String USERNAME_KEY = "USERNAME";

  /**
   * 协议建立之前添加认证信息
   *
   * @param sec      config
   * @param request  request
   * @param response response
   */
  @Override
  public void modifyHandshake(ServerEndpointConfig sec,
      HandshakeRequest request, HandshakeResponse response) {
    sec.getUserProperties().put(USERNAME_KEY, SubjectUtils.getCurrentUsername().orElse(null));
    super.modifyHandshake(sec, request, response);
  }

  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}