package rocket.starter.quartz.body;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import rocket.starter.common.validator.Create;
import rocket.starter.common.validator.Update;

/**
 * 定时任务操作数据.
 *
 * @author ron 2022/8/8
 */
@Data
public class QuartzJobBody {

  @Null(groups = Create.class)
  @NotNull(groups = Update.class)
  private Long id;

  /**
   * 名称.
   */
  @NotBlank
  private String name;

  /**
   * spring 中对应的bean name.
   */
  @NotBlank
  private String runnableBeanName;

  /**
   * 运行参数.
   */
  private String params;

  /**
   * cron 表达式, 决定如何执行.
   */
  @NotBlank
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
