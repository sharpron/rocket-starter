package pub.ron.admin.system.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseDto;

/**
 * menu info.
 *
 * @author ron 2020/12/14
 */
@Getter
@Setter
public class MenuDto extends BaseDto {

  private String title;

  private Integer orderNo;

  private String link;

  private String path;

  private String icon;

  private String component;

  private boolean hide;

  private String perm;

  private List<MenuDto> children;
}
