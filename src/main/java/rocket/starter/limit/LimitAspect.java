package rocket.starter.limit;

import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import rocket.starter.common.AppException;
import rocket.starter.limit.Limit.Type;
import rocket.starter.logging.utils.IpUtils;

/**
 * Treatment of current limiting.
 *
 * @author herong 2021/2/9
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LimitAspect {

  /**
   * redis key前缀.
   */
  private static final String KEY_PREFIX = "rate-limit:";

  private final StringRedisTemplate redisTemplate;

  /**
   * current request.
   */
  private final HttpServletRequest request;

  /**
   * 是否能够放行.
   *
   * @param key      key
   * @param period   间隔时间毫秒
   * @param maxCount 最大次数
   * @return true 可以放行
   */
  private boolean allowThrough(String key, int period, int maxCount) {
    DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
    redisScript.setScriptSource(
        new ResourceScriptSource(new ClassPathResource("/plugin/limit.lua")));
    redisScript.setResultType(Boolean.class);
    Boolean result = redisTemplate.execute(
        redisScript,
        Collections.singletonList(key),
        String.valueOf(period),
        String.valueOf(maxCount));
    assert result != null;
    return result;
  }

  /**
   * 使用环绕增强的方式处理限流.
   *
   * @param point 增强点
   * @return 方法调用结果
   * @throws Throwable 方法调用可能抛出异常
   */
  @Around("@annotation(rocket.starter.limit.Limit)")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    final MethodSignature signature = (MethodSignature) point.getSignature();
    final Limit limit = signature.getMethod().getAnnotation(Limit.class);

    final String key =
        KEY_PREFIX
            + (limit.type() == Type.METHOD_NAME
            ? signature.getName()
            : IpUtils.getClientIpAddress(request));
    if (allowThrough(key, limit.periodMills(), limit.maxCount())) {
      return point.proceed();
    }
    throw new AppException("服务器过于繁忙，稍候再试");
  }
}
