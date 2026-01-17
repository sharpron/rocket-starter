package rocket.starter.system.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rocket.starter.common.BaseDto;

/**
 * role dto.
 *
 * @author ron 2020/11/22
 */
@Getter
@Setter
@ToString
public class RoleDto extends BaseDto {

  private String name;

  private Boolean disabled;

  private String description;

  private Set<Long> deptIds;
  private Set<String> deptNames;

  private Set<Long> menuIds;
  private Set<String> menuTitles;

}
