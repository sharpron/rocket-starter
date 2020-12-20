package pub.ron.admin.system.domain;

import pub.ron.admin.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ron 2020/11/17
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sys_dept", indexes = {
    @Index(name = "idx_path", columnList = "path")
})
public class Dept extends BaseEntity {

  @Column(nullable = false)
  @NotBlank(message = "部门名称不能为空")
  private String name;

  @Column(nullable = false)
  private Integer orderNo;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Dept parent;

  @Column(nullable = false)
  private String path;

  public Dept(Long id) {
    super(id);
  }
}
