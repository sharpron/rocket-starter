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
@Table(name = "sys_user_role")
public class UserRole implements Serializable {

  @Id
  private Long userId;

  @Id
  private Long roleId;

}
