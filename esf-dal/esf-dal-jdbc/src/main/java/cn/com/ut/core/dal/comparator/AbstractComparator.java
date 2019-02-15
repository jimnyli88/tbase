package cn.com.ut.core.dal.comparator;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 比较器抽象基类
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 * @param <T>
 */
public abstract class AbstractComparator<T> implements Comparator<T> {

	private String orderBy;
	protected Map<String, Integer> orderBys = new LinkedHashMap<String, Integer>();

	public AbstractComparator(String orderBy) {

		this.orderBy = orderBy;
		transform();
		System.out.println("orderBys==" + orderBys);
	}

	private void transform() {

		if (orderBy == null || orderBy.trim().length() == 0)
			return;
		String[] orderByArray = orderBy.trim().split(",");
		for (String order : orderByArray) {
			order = order.trim();
			if (order.length() == 0)
				continue;
			String[] one = order.split(" ");
			if (one.length > 2 || one[0].trim().length() == 0)
				continue;

			if (one.length == 1)
				orderBys.put(one[0].trim(), 1);
			else if (one.length == 2) {

				if (one[1].trim().equalsIgnoreCase("desc")) {
					orderBys.put(one[0].trim(), -1);
				} else {
					orderBys.put(one[0].trim(), 1);
				}
			}
		}
	}

	protected int compare(Object a, Object b, int order) {

		if (a == b)
			return 0;
		if (a == null)
			return -1 * order;
		if (b == null)
			return 1 * order;

		// 忽略，不比较
		if (!a.getClass().equals(b.getClass()))
			return 0;
		// 忽略，不比较
		if (!Comparable.class.isAssignableFrom(a.getClass())
				|| !Comparable.class.isAssignableFrom(b.getClass()))
			return 0;
		return order * ((Comparable) a).compareTo(b);
	}

	public static void main(String[] args) {

	}

}
