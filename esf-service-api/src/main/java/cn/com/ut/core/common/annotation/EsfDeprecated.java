package cn.com.ut.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 过期将被新接口替代，请在注释写明新的接口(@see)
 * 
 * @author wuxiaohua
 * @since 2017年1月10日 下午6:02:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface EsfDeprecated {

	String version() default "1.0";
}
