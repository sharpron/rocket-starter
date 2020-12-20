package pub.ron.admin.system.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author ron 2020/12/12
 */
@Data
public class UserDto {

  private Long id;

  private Long deptId;

  private String deptName;

  private String username;

  private String mobile;

  private String email;

  private boolean disabled;

  private LocalDateTime createTime;

}
