package rocket.starter.system.security.properties;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 用户密码过期配置属性.
 *
 * @author ron 2022/9/25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.password-expire")
public class PasswordExpireProperties {

  /**
   * 是否启用密码过期. 默认不启用.
   */
  private boolean enabled = false;

  /**
   * 密码寿命. 默认90天.
   */
  private Duration lifetimeDuration = Duration.ofDays(90);
}
