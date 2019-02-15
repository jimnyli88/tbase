package cn.com.ut.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuxiaohua
 * @since 2015-07-14
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface ServiceComponent {

	/**
	 * 业务接口类型
	 * 
	 * @return 业务接口类型
	 */
	String type() default "";

	/**
	 * 业务对象或接口名称
	 * 
	 * @return 业务对象或接口名称
	 */
	String name() default "";

	/**
	 * 版本
	 * 
	 * @return 版本
	 */
	String version() default "1.0";

	/**
	 * 注释
	 * 
	 * @return 注释
	 */
	String comment() default "";

	/**
	 * 是否被忽略
	 * 
	 * @return 是否被忽略
	 */
	boolean ignore() default false;

	/**
	 * 是否作会话验证
	 * 
	 * @return 否作会话验证
	 */
	boolean session() default true;

	/**
	 * 是否作权限验证
	 * 
	 * @return 是否作权限验证
	 */
	boolean permission() default true;

	/**
	 * 是否开启日志
	 * 
	 * @return 是否开启跟踪日志
	 */
	boolean logger() default true;
}
