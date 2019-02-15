package cn.com.ut.core.dal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface DAOMethod {

	String table() default ""; // table

	String schema() default ""; // 逻辑schema

	boolean isShard() default false;

	String group() default "";
	
	boolean isWriteable() default true;

}
