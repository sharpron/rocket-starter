package pub.ron.admin.message.domain;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 * 消息查看信息.
 *
 * @author ron 2022/8/30
 */
@Data
@Embeddable
public class ReadInfo {

  private String user;
  private LocalDateTime time;
}
