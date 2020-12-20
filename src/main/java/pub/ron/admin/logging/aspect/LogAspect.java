package pub.ron.admin.logging.aspect;

import pub.ron.admin.logging.Log;
import pub.ron.admin.logging.service.LoggingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 扫描{@link Log}定义的public方法，处理日志行为
 *
 * @author ron 2020/9/20
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

  private final LoggingService loggingService;


  /**
   * 切点
   */
  @Pointcut("@annotation(pub.ron.admin.logging.Log)")
  public void pointCut() {
  }

  /**
   * 环绕增强
   *
   * @param point 增强点
   * @return 方法调用结果
   * @throws Throwable 方法调用可能抛出异常
   */
  @Around("pointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    return loggingService.addLogForOperation(
        point::proceed,
        LogAspect.getLogDef(point).value()
    );
  }

  /**
   * 从切入点获取Log注解
   *
   * @param point 切入点
   * @return 注解
   */
  private static Log getLogDef(JoinPoint point) {
    MethodSignature signature = (MethodSignature) point.getSignature();
    return signature.getMethod().getAnnotation(Log.class);
  }
}
