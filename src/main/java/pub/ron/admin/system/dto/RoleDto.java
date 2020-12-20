package pub.ron.admin.system.dto;

import pub.ron.admin.common.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ron 2020/11/22
 */
@Getter
@Setter
@ToString
public class RoleDto extends BaseDto {

  private String name;

  private String description;

  private Long deptId;

  private String deptName;
}
