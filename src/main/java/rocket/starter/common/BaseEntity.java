package rocket.starter.common;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 实体的基类.
 *
 * @author ron 2020/11/17
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  public static final String ID = "id";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, updatable = false)
  @CreatedBy
  private String createBy;

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createTime;

  @Column(nullable = false)
  @LastModifiedBy
  private String modifyBy;

  @Column(nullable = false)
  @LastModifiedDate
  private LocalDateTime modifyTime;

  public BaseEntity(Long id) {
    this.id = id;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (getClass() != o.getClass()) {
      return false;
    }
    BaseEntity that = (BaseEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(this.id);
  }

}
