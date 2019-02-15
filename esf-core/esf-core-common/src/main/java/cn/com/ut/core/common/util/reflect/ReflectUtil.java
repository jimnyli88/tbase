package cn.com.ut.core.common.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import cn.com.ut.core.common.util.CollectionUtil;

/**
 * 反射工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class ReflectUtil {

	private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	/**
	 * 使用反射获得private属性值
	 * 
	 * @param object
	 * @param fieldName
	 * @return string or ""
	 */
	public static String getValue(Object object, String fieldName) {

		String value = "";
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			value = field.get(object) == null ? "" : (String) field.get(object);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * 获取bean
	 * 
	 * @param ctx
	 * @param localBeanName
	 * @param remoteBeanName
	 * @return object or null
	 */
	public static Object getBean(ApplicationContext ctx, String localBeanName,
			String remoteBeanName) {

		Object bean = null;
		try {
			bean = ctx.getBean(localBeanName);
		} catch (BeansException e) {
			try {
				bean = ctx.getBean(remoteBeanName);
			} catch (BeansException ex) {
			}
		}
		return bean;
	}

	/**
	 * 反射调用业务方法
	 * 
	 * @param serviceObj
	 * @param methodName
	 * @param parameterTypes
	 * @param parameterValues
	 * @return Object
	 */
	public static Object invokeServiceMethod(Object serviceObj, String methodName,
			Class<?>[] parameterTypes, Object[] parameterValues) {

		Class<?> clazz = null;
		Object result = null;
		Method method = null;

		try {
			clazz = serviceObj.getClass();
			method = clazz.getDeclaredMethod(methodName, parameterTypes);
			result = method.invoke(serviceObj, parameterValues);
			logger.debug("reflect invoke {}#{}", clazz.getName(), methodName);
		} catch (InvocationTargetException e) {
			logger.error(e.getTargetException().getMessage());
			e.getTargetException().printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 反射调用业务方法
	 * 
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @param parameterValues
	 * @param ctx
	 * @return Object
	 */
	public static Object invokeServiceMethod(String className, String methodName,
			Class<?>[] parameterTypes, Object[] parameterValues, ApplicationContext ctx) {

		Class<?> clazz = null;
		Object serviceObj = null;
		Object result = null;
		Method method = null;

		try {
			clazz = Class.forName(className);
			method = clazz.getDeclaredMethod(methodName, parameterTypes);
			serviceObj = ctx.getBean(clazz);
			result = method.invoke(serviceObj, parameterValues);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error(e.getTargetException().getMessage());
			e.getTargetException().printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	public static Object invokeServiceMethod(String className, String methodName,
			Map<String, Object> parameters, ApplicationContext ctx) {

		Class<?> clazz = null;
		Object serviceObj = null;
		Object result = null;
		Method method = null;

		try {

			List<Class<?>> parameterTypes = new ArrayList<>();
			if (!CollectionUtil.isEmptyMap(parameters)) {
				Set<String> parameterTypeNames = parameters.keySet();
				for (String parameterTypeName : parameterTypeNames) {
					parameterTypes.add(classNameToType(parameterTypeName.split("@")[1]));
				}
			}

			clazz = Class.forName(className);
			method = clazz.getDeclaredMethod(methodName,
					parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
			serviceObj = ctx.getBean(clazz);
			result = method.invoke(serviceObj, parameters.values().toArray());
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new ReflectException(e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error(e.getTargetException().getMessage());
			e.getTargetException().printStackTrace();
			throw new ReflectException(e.getTargetException().getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new ReflectException(e.getMessage());
		}

		return result;
	}

	/**
	 * 静态方法调用
	 * 
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @param parameterValues
	 * @return Object
	 */
	public static Object invokeStatic(String className, String methodName,
			Class<?>[] parameterTypes, Object... parameterValues) {

		try {
			Class<?> clazz = Class.forName(className);
			Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
			Object result = method.invoke(null, parameterValues);
			return result;
		} catch (Exception e) {
			throw new ReflectException(e);
		}
	}

	public static Class<?> classNameToType(String className) throws ClassNotFoundException {

		if (isPrimitiveType(className)) {
			return getPrimitiveType(className);
		} else {
			return Class.forName(className);
		}
	}

	/**
	 * 根据className获取基本类型
	 * 
	 * @param className
	 * @return
	 */
	public static Class<?> getPrimitiveType(String className) {

		if (int.class.getName().equals(className)) {
			return int.class;
		} else if (boolean.class.getName().equals(className)) {
			return boolean.class;
		} else if (char.class.getName().equals(className)) {
			return char.class;
		} else if (byte.class.getName().equals(className)) {
			return byte.class;
		} else if (short.class.getName().equals(className)) {
			return short.class;
		} else if (long.class.getName().equals(className)) {
			return long.class;
		} else if (float.class.getName().equals(className)) {
			return float.class;
		} else if (double.class.getName().equals(className)) {
			return double.class;
		} else if (void.class.getName().equals(className)) {
			return void.class;
		} else {
			return null;
		}
	}

	/**
	 * 判断className是否为基本类型
	 * 
	 * @param className
	 * @return
	 */
	public static boolean isPrimitiveType(String className) {

		return int.class.getName().equals(className) || boolean.class.getName().equals(className)
				|| char.class.getName().equals(className) || byte.class.getName().equals(className)
				|| short.class.getName().equals(className) || long.class.getName().equals(className)
				|| float.class.getName().equals(className)
				|| double.class.getName().equals(className)
				|| void.class.getName().equals(className);
	}
}