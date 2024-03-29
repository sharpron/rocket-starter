package rocket.starter.system.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rocket.starter.common.BaseDto;

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

  private Set<Long> roleIds;

  private String username;

  private String nickname;

  private String mobile;

  private String email;

  private boolean disabled;

  private boolean locked;
}
