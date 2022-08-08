package pub.ron.admin.system.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pub.ron.admin.common.BaseDto;

/**
 * dept dto.
 *
 * @author ron 2020/11/19
 */
@Getter
@Setter
@ToString
public class DeptDto extends BaseDto {

  private Long id;

  private String name;

  private Long parentId;

  private Integer orderNo;

  private List<DeptDto> children;
}
