package rocket.starter.system.dto;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rocket.starter.common.BaseDto;
import rocket.starter.common.utils.TreeUtils.Node;

/**
 * dept dto.
 *
 * @author ron 2020/11/19
 */
@Getter
@Setter
@ToString
public class DeptDto extends BaseDto implements Node<DeptDto> {

  private Long id;

  private String name;

  private Long parentId;

  private Boolean disabled;

  private Integer orderNo;

  private List<DeptDto> children;

  @Override
  public boolean childrenOf(DeptDto parent) {
    return Objects.equals(parentId, parent.id);
  }

  @Override
  public void addChildren(DeptDto child) {
    children.add(child);
  }
}
