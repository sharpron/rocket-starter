package pub.ron.admin.system.domain;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import pub.ron.admin.common.BaseEntity;

/**
 * @author ron 2020/11/17
 */
@Getter
@Setter
@Entity
@Table(name = "sys_user")
@DynamicUpdate
public class User extends BaseEntity {

  public static final String ADMIN = "admin";

  @Column(
      unique = true,
      columnDefinition = "char(10)",
      updatable = false
  )
  private String username;

  @Column(columnDefinition = "char(64)")
  private String password;

  @Column(columnDefinition = "char(36)")
  private String passwordSalt;

  @Column(columnDefinition = "char(11)")
  @NotBlank
  private String mobile;

  @Email(message = "邮箱格式错误")
  private String email;

  private Boolean locked;

  private Boolean disabled;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
  @NotNull(message = "必须指定部门")
  private Dept dept;

  @PrePersist
  public void prePersist() {
    if (this.disabled == null) {
      this.disabled = Boolean.FALSE;
    }
    this.locked = Boolean.FALSE;
  }
}
