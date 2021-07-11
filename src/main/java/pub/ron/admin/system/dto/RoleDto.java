package pub.ron.admin.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pub.ron.admin.common.BaseDto;

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

  private String description;

  private Long deptId;

  private String deptName;
}
