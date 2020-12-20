package pub.ron.admin.system.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author ron 2020/12/14
 */
@Data
@Entity
@Table(name = "sys_role_menu")
public class RoleMenu implements Serializable {

  @Id
  private Long roleId;

  @Id
  private Long menuId;

}
