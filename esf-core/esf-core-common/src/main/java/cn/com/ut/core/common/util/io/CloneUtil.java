package cn.com.ut.core.common.util.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
* CloneUtil
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class CloneUtil {

	/**
	 *
	 */
	private CloneUtil() {

	}

	/**
	 * 
	 * @param obj
	 * @return object or null
	 * @throws CloneNotSupportedException
	 */
	public static <T> T cloneObject(final T obj) throws CloneNotSupportedException {

		if (obj == null) {
			return null;
		}
		if (obj instanceof Cloneable) {
			final Class<?> clazz = obj.getClass();
			final Method m;
			try {
				m = clazz.getMethod("clone", (Class[]) null);
			} catch (final NoSuchMethodException ex) {
				throw new NoSuchMethodError(ex.getMessage());
			}
			try {
				@SuppressWarnings("unchecked")
				// OK because clone() preserves the class
				final T result = (T) m.invoke(obj, (Object[]) null);
				return result;
			} catch (final InvocationTargetException ex) {
				final Throwable cause = ex.getCause();
				if (cause instanceof CloneNotSupportedException) {
					throw ((CloneNotSupportedException) cause);
				} else {
					throw new Error("Unexpected exception", cause);
				}
			} catch (final IllegalAccessException ex) {
				throw new IllegalAccessError(ex.getMessage());
			}
		} else {
			throw new CloneNotSupportedException();
		}
	}

	/**
	 * 
	 * @param obj
	 * @return object
	 * @throws CloneNotSupportedException
	 */
	public static Object clone(final Object obj) throws CloneNotSupportedException {

		return cloneObject(obj);
	}

}
