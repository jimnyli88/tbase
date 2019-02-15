package cn.com.ut.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
public @interface Cached {

	boolean cacheable() default true;

	String key() default "";

	int expire() default 60 * 20;

	// @Cached(evict = {"a","b"})
	String[] evict() default {};
}
