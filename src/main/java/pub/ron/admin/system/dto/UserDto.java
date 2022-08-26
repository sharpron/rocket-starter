package pub.ron.admin.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pub.ron.admin.common.BaseDto;

/**
 * user dto.
 *
 * @author ron 2020/12/12
 */
@Getter
@Setter
@ToString
public class UserDto extends BaseDto {

  private Long id;

  private Long deptId;

  private String deptName;

  private String username;

  private String nickname;

  private String mobile;

  private String email;

  private boolean disabled;

  private boolean locked;
}
