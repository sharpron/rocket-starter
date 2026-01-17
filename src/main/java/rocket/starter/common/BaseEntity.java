package rocket.starter.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;

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
  @NotNull(groups = Update.class)
  @Null(groups = Create.class)
  private Long id;

  @Column(nullable = false, updatable = false)
  @CreatedBy
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String createBy;

  @Column(nullable = false, updatable = false)
  @CreatedDate
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime createTime;

  @Column(nullable = false)
  @LastModifiedBy
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String modifyBy;

  @Column(nullable = false)
  @LastModifiedDate
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
