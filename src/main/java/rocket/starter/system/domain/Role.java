package rocket.starter.system.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rocket.starter.common.BaseEntity;

/**
 * role.
 *
 * @author ron 2020/11/17
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "sys_role")
public class Role extends BaseEntity {

  @Column(length = 20)
  private String name;

  private String description;

  private Boolean disabled;

  @ManyToMany
  @JoinTable(name = "sys_role_dept",
      joinColumns = @JoinColumn(name = "role_id",
          foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
      inverseJoinColumns = @JoinColumn(name = "dept_id",
          foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Set<Dept> departments;

  @ManyToMany
  @JoinTable(name = "sys_role_menu",
      joinColumns = @JoinColumn(name = "role_id",
          foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
      inverseJoinColumns = @JoinColumn(name = "menu_id",
          foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)))
  private Set<Menu> menus;

  /**
   * pre persist.
   */
  @PrePersist
  public void prePersist() {
    if (this.disabled == null) {
      this.disabled = Boolean.FALSE;
    }
  }
}
