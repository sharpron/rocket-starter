package pub.ron.admin.config;

import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import pub.ron.admin.system.security.SubjectUtils;

/**
 * 操作员记录器.
 *
 * @author ron 2020.10.21
 */
@Configuration
public class OperatorRecorder implements AuditorAware<String> {

  /**
   * 给Bean中的 @CreatedBy @LastModifiedBy 注入操作人.
   */
  @Override
  public Optional<String> getCurrentAuditor() {
    return SubjectUtils.getCurrentUsername().or(() -> Optional.of("System"));
  }
}
