package pub.ron.admin.system.dto;

import pub.ron.admin.common.BaseDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ron 2020/11/19
 */
@Getter
@Setter
@ToString
public class DeptDto extends BaseDto {

  private Long id;

  private String name;

  private Integer orderNo;

  private List<DeptDto> children;

}
