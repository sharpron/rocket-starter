package pub.ron.admin.system.security.properties;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 用户登录失败锁定配置属性.
 *
 * @author ron 2022/9/25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.lock")
public class LockProperties {

  /**
   * 是否启用锁定. 默认不启用.
   */
  private boolean enabled = false;

  /**
   * 最大尝试登录次数. 默认5次.
   */
  private int maxTryTimes = 5;

  /**
   * 锁定时间. 默认30分钟
   */
  private Duration lockDuration = Duration.ofMinutes(30);
}
