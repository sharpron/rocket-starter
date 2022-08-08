package pub.ron.admin.system.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * menu info.
 *
 * @author ron 2020/12/14
 */
@Getter
@Setter
public class MenuSmallDto {

  private Long id;

  private String title;

  private List<MenuSmallDto> children;
}
