package cn.com.ut.core.dal.comparator;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Map集合类型比较器
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
public class MapComparator extends AbstractComparator<Map<String, Object>> {

	public MapComparator(String orderBy) {

		super(orderBy);
	}

	@Override
	public int compare(Map<String, Object> a, Map<String, Object> b) {

		if (orderBys.isEmpty())
			return 0;

		Set<Entry<String, Integer>> set = orderBys.entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		Entry<String, Integer> entry = null;
		int result = 0;
		while (iter.hasNext()) {
			entry = iter.next();
			int temp = compare(a.get(entry.getKey()), b.get(entry.getKey()), entry.getValue());
			if (temp == 0)
				result = 0;
			else
				return temp;
		}
		return result;
	}

	public static void main(String[] args) {

		MapComparator mc = new MapComparator(null);
		Object oa = "aa";
		Object ob = "bb";
		Object oc = "cc";
		System.out.println(mc.compare(oa, ob, 0));
		System.out.println(mc.compare(oa, ob, 1));
		System.out.println(mc.compare(oc, ob, 0));
		System.out.println(mc.compare(oc, ob, 1));
		System.out.println(mc.compare(oc, oc, 1));
		System.out.println(mc.compare(oc, oc, 0));
	}

}
