package rocket.starter.system.security;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import rocket.starter.system.security.properties.LockProperties;

/**
 * 用户锁定管理.
 *
 * @author ron 2022/8/24
 */
@Component
@RequiredArgsConstructor
public class DefaultUserLocker implements UserLocker {

  private static final String AUTH_FAILS_PREFIX = "user:auth-fails:";

  private final StringRedisTemplate redisTemplate;

  private final LockProperties lockProperties;


  @Override
  public boolean isLocked(String username) {
    if (lockProperties.isEnabled()) {
      String fails = redisTemplate.opsForValue().get(AUTH_FAILS_PREFIX + username);
      if (fails == null) {
        return false;
      }
      return Integer.parseInt(fails) >= lockProperties.getMaxTryTimes();
    }

    return false;
  }

  @Override
  public boolean tryLocked(String username) {
    if (!lockProperties.isEnabled()) {
      return false;
    }

    String key = AUTH_FAILS_PREFIX + username;

    Object incrementResult = redisTemplate.execute(new SessionCallback<>() {
      @Override
      public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
        redisTemplate.multi();
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, lockProperties.getLockDuration());
        return redisTemplate.exec().get(0);
      }
    });
    assert incrementResult != null;
    int fails = ((Number) incrementResult).intValue();
    return fails >= lockProperties.getMaxTryTimes();
  }

  @Override
  public int getMaxTryTimes() {
    return lockProperties.getMaxTryTimes();
  }

  @Override
  public Duration getLockDuration() {
    return lockProperties.getLockDuration();
  }

  @Override
  public void unlock(String username) {
    redisTemplate.delete(AUTH_FAILS_PREFIX + username);
  }
}
