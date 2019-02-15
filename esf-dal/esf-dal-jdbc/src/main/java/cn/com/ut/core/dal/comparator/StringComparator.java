package cn.com.ut.core.dal.comparator;

/**
 * 字符类型比较器
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
public class StringComparator extends AbstractComparator<String> {

	private int order;

	public StringComparator(String orderBy) {

		super(orderBy);
	}

	public StringComparator(int order) {

		super(null);
		this.order = order;
	}

	@Override
	public int compare(String a, String b) {

		if (a == b)
			return 0;
		if (a == null)
			return order == 0 ? -1 : 1;
		if (b == null)
			return order == 0 ? 1 : -1;

		if (order == 0)
			return a.compareTo(b);
		else
			return -1 * a.compareTo(b);
	}

	public static void main(String[] args) {

	}
}
