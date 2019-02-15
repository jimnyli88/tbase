package cn.com.ut.redisson.lock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxiaohua
 * @version 1.0
 * @date 2019/01/10 11:16
 */
@Slf4j
@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class LockInterceptor {

	@Autowired(required = false)
	private RedissonLocker redissonLocker;

	@Around("@annotation(cn.com.ut.redisson.lock.LockedBy)")
	public Object aroundLockMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {

		Signature signature = pjp.getSignature();
		MethodSignature ms = (MethodSignature) signature;
		Object[] args = pjp.getArgs();
		Object result = null;
		Method method = ms.getMethod();
		// 解析LockedBy注解
		LockedBy lockedBy = ms.getMethod().getAnnotation(LockedBy.class);
		String lockKey = lockedBy.lockKey();

		// 解析LockKey注解
		// Annotation[][] annotations = method.getParameterAnnotations();
		// for (int i = 0; i < annotations.length; i++) {
		// if (annotations[i].length == 1
		// && annotations[i][0].annotationType().equals(LockKey.class) && args[i] !=
		// null
		// && args[i] instanceof String) {
		// lockKey = String.valueOf(args[i]);
		// }
		// }

		for (int i = 0; i < method.getParameters().length; i++) {
			if (method.getParameters()[i].isAnnotationPresent(LockKey.class)) {
				lockKey = (args[i] == null ? "" : String.valueOf(args[i]));
			}
		}

		if (lockKey == null || "".equals(lockKey.trim())) {
			String className = pjp.getTarget().getClass().getName();
			String methodName = ms.getName();
			lockKey = new StringBuilder("/").append(className).append("/").append(methodName)
					.toString();
		}

		// debug log
		log.debug("==lockMethod:lockKey==" + signature.toShortString() + ":" + lockKey);

		// 加锁
		LockHold lockHold = redissonLocker.tryLock(lockKey,
				lockedBy.waitTime() < 0L ? null : lockedBy.waitTime(),
				lockedBy.leaseTime() < 0L ? null : lockedBy.leaseTime(), lockedBy.randomDelay(),
				lockedBy.lockEnum());
		try {

			if (lockHold.isLocked()) {
				result = pjp.proceed();
			}

		} finally {
			// 释放锁
			if (lockHold.isLocked()) {
				// Sleep
				if (lockedBy.sleepTime() > 0L) {
					try {
						TimeUnit.MILLISECONDS.sleep(lockedBy.sleepTime());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// unlock
				lockHold.unlock();
				log.debug("==unlock==");
			}
		}

		return result;
	}
}
