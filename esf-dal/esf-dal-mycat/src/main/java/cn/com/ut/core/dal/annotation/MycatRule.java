package cn.com.ut.core.dal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface MycatRule {

	/**
	 * 是否开启XA分布式事务，默认不开启
	 * 
	 */
	boolean xa() default false;

	/**
	 * 是否强行访问主库（写库）
	 */
	boolean master() default true;

}
