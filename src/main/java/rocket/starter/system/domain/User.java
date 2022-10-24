package rocket.starter.system.domain;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

import java.time.LocalDateTime;
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
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import rocket.starter.common.BaseEntity;

/**
 * user.
 *
 * @author ron 2020/11/17
 */
@Getter
@Setter
@Entity
@Table(name = "sys_user")
@DynamicUpdate
public class User extends BaseEntity {

  public static final String ADMIN = "admin";

  @Column(unique = true, columnDefinition = "char(10)", updatable = false)
  private String username;

  private String nickname;

  @Column(columnDefinition = "char(64)", updatable = false)
  private String password;

  @Column(columnDefinition = "char(36)", updatable = false)
  private String passwordSalt;

  /**
   * 密码过期时间.
   */
  private LocalDateTime passwordExpireAt;

  @Column(columnDefinition = "char(11)")
  private String mobile;

  private String email;

  private Boolean disabled;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(NO_CONSTRAINT))
  private Dept dept;

  @ElementCollection
  @CollectionTable(name = "sys_user_role", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @Column(name = "role_id")
  private Set<Long> roleIds;

  /**
   * pre persist.
   */
  @PrePersist
  public void prePersist() {
    if (this.disabled == null) {
      this.disabled = Boolean.FALSE;
    }
  }

  @Transient
  public boolean isAdmin() {
    return ADMIN.equals(username);
  }
}
