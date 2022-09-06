package pub.ron.admin.system.security;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户锁定管理.
 *
 * @author ron 2022/8/24
 */
@Component
@RequiredArgsConstructor
public class DefaultUserLocker implements UserLocker {

  private static final String AUTH_FAILS_PREFIX = "user:auth-fails:";
  private static final int MAX_TRY_TIMES = 5;

  private static final Duration LOCK_DURATION = Duration.ofHours(1);
  private final StringRedisTemplate redisTemplate;


  @Override
  public boolean isLocked(String username) {
    String fails = redisTemplate.opsForValue().get(AUTH_FAILS_PREFIX + username);
    if (fails == null) {
      return false;
    }
    return Integer.parseInt(fails) >= MAX_TRY_TIMES;
  }

  @Override
  public boolean checkLockedWhenFail(String username) {
    String key = AUTH_FAILS_PREFIX + username;

    Object incrementResult = redisTemplate.execute(new SessionCallback<>() {
      @Override
      public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
        redisTemplate.multi();
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, LOCK_DURATION);
        return redisTemplate.exec().get(0);
      }
    });
    assert incrementResult != null;
    int fails = ((Number) incrementResult).intValue();
    return fails >= MAX_TRY_TIMES;
  }

  @Override
  public int getMaxTryTimes() {
    return MAX_TRY_TIMES;
  }

  @Override
  public Duration getLockDuration() {
    return LOCK_DURATION;
  }

  @Override
  public void unlock(String username) {
    redisTemplate.delete(AUTH_FAILS_PREFIX + username);
  }
}
