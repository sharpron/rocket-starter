package pub.ron.admin.system.dto;

import pub.ron.admin.common.BaseDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
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
