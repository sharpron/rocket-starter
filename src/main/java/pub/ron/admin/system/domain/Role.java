package pub.ron.admin.system.domain;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseEntity;

/**
 * role.
 *
 * @author ron 2020/11/17
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity {

  @Column(length = 20)
  @NotBlank(message = "角色名称不能为空")
  private String name;

  private String description;

  private Boolean disabled;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @NotNull(message = "必须指定部门")
  private Dept dept;

  @ElementCollection
  @CollectionTable(name = "sys_role_dept", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @Column(name = "dept_id")
  private Set<Long> deptIds;

  @ElementCollection
  @CollectionTable(name = "sys_role_menu", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @Column(name = "menu_id")
  private Set<Long> menuIds;

  /** pre persist. */
  @PrePersist
  public void prePersist() {
    if (this.disabled == null) {
      this.disabled = Boolean.FALSE;
    }
  }
}
