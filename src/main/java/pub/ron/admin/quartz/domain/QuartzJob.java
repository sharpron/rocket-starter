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

  /**
   * 名称.
   */
  private String name;

  /**
   * spring 中对应的bean name.
   */
  private String runnableBeanName;

  /**
   * 运行参数.
   */
  private String params;

  /**
   * cron 表达式, 决定如何执行.
   */
  private String cronExpression;

  /**
   * 启用和禁用状态.
   */
  private boolean enabled;

  /**
   * 描述信息.
   */
  private String description;
}
