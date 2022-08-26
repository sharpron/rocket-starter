package pub.ron.admin.system.dto;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.utils.TreeUtils.Node;

/**
 * menu info.
 *
 * @author ron 2020/12/14
 */
@Getter
@Setter
public class MenuSmallDto implements Node<MenuSmallDto> {

  private Long id;

  private Long parentId;

  private String title;

  private List<MenuSmallDto> children;

  @Override
  public boolean childrenOf(MenuSmallDto parent) {
    return Objects.equals(parentId, parent.id);
  }

  @Override
  public void addChildren(MenuSmallDto child) {
    children.add(child);
  }
}
