package cn.com.ut.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.ut.core.common.constant.DialectConstant;

/**
 * 在业务方法上标识使用了某种数据库的特定功能，例如mysql的内置函数、存储过程等
 * 
 * @author wuxiaohua
 * @since 2017年1月10日 下午6:02:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface Dialect {

	/**
	 * 当前数据库方言
	 */
	String value() default DialectConstant.PGSQL;

	/**
	 * 是否支持多方言
	 */
	boolean multiDialectSupport() default false;
}
