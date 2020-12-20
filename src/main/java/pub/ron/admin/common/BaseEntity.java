package pub.ron.admin.common;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author ron 2020/11/17
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

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

}
