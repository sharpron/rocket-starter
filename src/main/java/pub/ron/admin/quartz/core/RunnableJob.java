package pub.ron.admin.quartz.core;

/**
 * 自定义的作业的入口，强制规范化.
 *
 * @author herong 2021/2/9
 */
public interface RunnableJob {

  /**
   * 运行任务.
   *
   * @param args 参数
   */
  void run(String args);
}
