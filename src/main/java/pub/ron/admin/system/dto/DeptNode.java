package pub.ron.admin.system.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * dept node.
 *
 * @author ron 2020/11/19
 */
@Getter
@Setter
@ToString
public class DeptNode {

  private Long id;

  private String name;

  private List<DeptNode> children;
}
