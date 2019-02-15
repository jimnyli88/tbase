package cn.com.ut.core.dal.comparator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeanUtils;

/**
 * 通用Bean属性比较器
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 * @param <T>
 */
public class BeanComparator<T> extends AbstractComparator<T> {

	private Class<T> mappedClass;
	private Map<String, PropertyDescriptor> mappedFields;
	private Set<String> mappedProperties;

	public BeanComparator(String orderBy, Class<T> mappedClass) {

		super(orderBy);
		initialize(mappedClass);
	}

	protected void initialize(Class<T> mappedClass) {

		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, PropertyDescriptor>();
		this.mappedProperties = new HashSet<String>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(pd.getName().toLowerCase(), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					this.mappedFields.put(underscoredName, pd);
				}
				this.mappedProperties.add(pd.getName());
			}
		}
	}

	private static String underscoreName(String name) {

		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				} else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}

	private Object getProperty(T t, String name) {

		PropertyDescriptor pd = this.mappedFields.get(name.toLowerCase());
		Method readMethod = pd.getReadMethod();

		if (readMethod == null)
			return null;

		Object result = null;

		try {
			result = readMethod.invoke(t);
		} catch (IllegalAccessException e) {

		} catch (IllegalArgumentException e) {

		} catch (InvocationTargetException e) {

		}

		return result;
	}

	@Override
	public int compare(T a, T b) {

		if (orderBys.isEmpty())
			return 0;

		Set<Entry<String, Integer>> set = orderBys.entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		Entry<String, Integer> entry = null;
		int result = 0;
		while (iter.hasNext()) {
			entry = iter.next();
			Object pa = getProperty(a, entry.getKey());
			Object pb = getProperty(b, entry.getKey());
			int temp = compare(pa, pb, entry.getValue());
			if (temp == 0)
				result = 0;
			else
				return temp;
		}
		return result;
	}

	public static void main(String[] args) {

	}
}
