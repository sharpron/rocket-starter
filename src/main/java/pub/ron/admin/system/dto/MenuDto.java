package pub.ron.admin.system.dto;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseDto;
import pub.ron.admin.common.utils.TreeUtils.Node;
import pub.ron.admin.system.domain.MenuType;

/**
 * menu info.
 *
 * @author ron 2020/12/14
 */
@Getter
@Setter
public class MenuDto extends BaseDto implements Node<MenuDto> {

  private String title;

  private Integer orderNo;

  private Long parentId;

  private MenuType type;

  private String path;

  private Boolean cacheable;

  private String icon;

  private boolean hide;

  private String perm;

  private List<MenuDto> children;

  @Override
  public boolean childrenOf(MenuDto parent) {
    return Objects.equals(parentId, parent.getId());
  }

  @Override
  public void addChildren(MenuDto menuDto) {
    children.add(menuDto);
  }

}
