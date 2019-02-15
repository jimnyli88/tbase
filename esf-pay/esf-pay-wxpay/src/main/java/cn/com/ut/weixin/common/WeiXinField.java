package cn.com.ut.weixin.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据结构属性注解
 * @author ouyuexing
 * @since 2017年6月28日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface WeiXinField {

	/** JSON属性映射名称 **/
	public String value() default "";
}
