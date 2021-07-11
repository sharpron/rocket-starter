package pub.ron.admin.quartz.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pub.ron.admin.common.BaseEntity;

/**
 * quartz job.
 *
 * @author ron 2021-05-31
 */
@Getter
@Setter
@Entity
@Table(name = "quartz_job")
public class QuartzJob extends BaseEntity {

  private String name;

  private String runnableBeanName;

  private String params;

  private String cronExpression;

  private boolean enabled;

  private String personInCharge;

  private String emails;

  private boolean continueWhenFail;

  private String description;
}
