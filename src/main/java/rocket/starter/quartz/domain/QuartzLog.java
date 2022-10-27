package rocket.starter.quartz.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

/**
 * quartz log.
 *
 * @author ron 2021-05-31
 */
@Entity
@Getter
@Setter
@Table(name = "quartz_log")
public class QuartzLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long jobId;

  private boolean success;

  @Column(columnDefinition = "text")
  private String exceptionDetail;

  /**
   * 执行花费时间.
   */
  private Long duration;

  @CreatedDate
  private LocalDateTime createTime;
}