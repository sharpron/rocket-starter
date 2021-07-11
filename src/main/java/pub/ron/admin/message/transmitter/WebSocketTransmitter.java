package pub.ron.admin.message.transmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pub.ron.admin.common.utils.JsonUtils;
import pub.ron.admin.config.WebSocketConfig;
import pub.ron.admin.message.Message;
import pub.ron.admin.message.Transmitter;

/**
 * 使用web socket进行消息通知.
 *
 * @author herong
 */
@Slf4j
@ServerEndpoint(value = "/socket/messages")
@Component
public class WebSocketTransmitter implements Transmitter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  /** 记录活跃的session. */
  private final ConcurrentMap<String, Session> activeSessions = new ConcurrentHashMap<>();

  /** 记录当前在线连接数. */
  private final AtomicInteger onlineCount = new AtomicInteger();

  /**
   * 获取session携带的用户名.
   *
   * @param session session
   * @return 用户名
   */
  private String getUsername(Session session) {
    return (String) session.getUserProperties().get(WebSocketConfig.USERNAME_KEY);
  }

  /** 连接建立成功调用的方法. */
  @OnOpen
  public void onOpen(Session session) {
    final String username = getUsername(session);
    activeSessions.putIfAbsent(username, session);
    log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.incrementAndGet());
  }

  /** 连接关闭调用的方法. */
  @OnClose
  public void onClose(Session session) {
    final String username = getUsername(session);
    activeSessions.remove(username);
    log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.decrementAndGet());
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("消息发生错误", error);
  }

  /** 服务端发送消息给客户端. */
  private void sendMessage(String message, Session toSession) {
    try {
      log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
      toSession.getBasicRemote().sendText(message);
    } catch (Exception e) {
      log.error("服务端发送消息给客户端失败", e);
    }
  }

  @Override
  public void transmit(Message message) {
    final Session session = activeSessions.get(message.getReceiverContact());
    if (session != null) {
      try {
        session.getBasicRemote().sendObject(JsonUtils.toString(message));
      } catch (IOException e) {
        log.error("网络发生异常", e);
      } catch (EncodeException e) {
        throw new AssertionError(e);
      }
    } else {
      log.warn("没有找到对应的session {}", message.getReceiverContact());
    }
  }
}
