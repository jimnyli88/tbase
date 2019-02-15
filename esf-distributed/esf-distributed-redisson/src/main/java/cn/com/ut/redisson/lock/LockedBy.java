package cn.com.ut.redisson.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuxiaohua
 * @version 1.0
 * @date 2019/01/10 11:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.PARAMETER })
public @interface LockedBy {

	long waitTime() default -1L;

	long leaseTime() default -1L;

	long sleepTime() default -1L;

	boolean randomDelay() default false;

	LockEnum lockEnum() default LockEnum.Lock;

	String lockKey() default "";
}
