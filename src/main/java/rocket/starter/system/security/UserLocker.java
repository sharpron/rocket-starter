package rocket.starter.system.security;

import java.time.Duration;

/**
 * 用户锁定管理.
 *
 * @author ron 2022/8/24
 */
public interface UserLocker {

  /**
   * 检查是否锁定.
   *
   * @param username 用户名
   * @return 是否锁定
   */
  boolean isLocked(String username);

  /**
   * 尝试对用户进行锁定，当超过最大次数将锁定并返回true，否则不处理并返回false.
   *
   * @param username 用户名
   * @return 是否锁定
   */
  boolean tryLocked(String username);

  /**
   * 获取最大尝试次数.
   *
   * @return 最大尝试次数
   */
  int getMaxTryTimes();

  /**
   * 获取锁定时间.
   *
   * @return 锁定时间
   */
  Duration getLockDuration();


  /**
   * 手动解锁用户.
   *
   * @param username 用户名
   */
  void unlock(String username);
}
