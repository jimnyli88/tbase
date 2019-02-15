package cn.com.ut.core.common.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Assert;
/**
* 表达式操作
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class ExpressionOperator {

	/**
	 * 对后缀表达式进行运算
	 * 
	 * @param sort
	 * @param map
	 * @return StringBuilder
	 */
	public static StringBuilder operator(List<String> sort, Map<String, String> map) {

		LinkedList<StringBuilder> q = new LinkedList<StringBuilder>();
		for (String ss : sort) {
			if (regex(ss)) {
				StringBuilder value = new StringBuilder(map.get(ss));
				if ((value.indexOf("AND") != -1 || value.indexOf("and") != -1
						|| value.indexOf("OR") != -1 || value.indexOf("or") != -1)
						&& value.charAt(0) != '(') {
					value.insert(0, '(');
					value.append(')');
				}
				q.addLast(value);
			} else {
				StringBuilder b = q.pollLast();
				StringBuilder a = q.pollLast();
				StringBuilder c = null;
				if ("*".equals(ss)) {
					c = ConditionBuilder.andOperator(a, b);
				} else if ("#".equals(ss)) {
					c = ConditionBuilder.orOperator(a, b);
				}
				q.addLast(c);
			}
		}
		return q.getLast();
	}

	/**
	 * 中缀表达式转后缀表达式
	 * 
	 * @param infix
	 * @return List<String>
	 */
	public static List<String> toPostfix(String exp) {

		List<String> array = format(exp);
		List<String> sort = new ArrayList<String>();
		LinkedList<String> q = new LinkedList<String>();
		for (String s : array) {
			if (regex(s)) {
				sort.add(s);
			} else {
				if (q.isEmpty()) {
					q.addLast(s);
				} else {
					if ("(".equals(s)) {
						q.addLast(s);
					} else if (")".equals(s)) {
						while (!q.isEmpty()) {
							String t = q.pollLast();
							if (!t.equals("(")) {
								sort.add(t);
							} else {
								break;
							}
						}
					} else if ("*".equals(s) || "#".equals(s)) {
						while (!q.isEmpty()) {
							if (q.peekLast().equals("*") || q.peekLast().equals("#")) {
								sort.add(q.pollLast());
							} else {
								break;
							}
						}
						q.addLast(s);
					}
				}
			}
		}

		while (!q.isEmpty()) {
			sort.add(q.pollLast());
		}

		return sort;
	}

	/**
	 * 表达式格式化，trim操作，字符分隔得到id(大于0的整数，位数1位或以上)和运算符
	 * 
	 * @param expression
	 * @return List<String>
	 */
	private static List<String> format(String exp) {

		char[] array = exp.toCharArray();
		LinkedList<String> link = new LinkedList<String>();
		for (char c : array) {
			String s = Character.toString(c);
			if ('0' <= c && c <= '9') {
				String prev = link.peekLast();
				if (prev == null) {
					if (c != '0')
						link.addLast(s);
				} else {
					if (regex(prev)) {
						prev = prev + s;
						link.removeLast();
						link.addLast(prev);
					} else {
						if (c != '0')
							link.addLast(s);
					}
				}

			} else if (' ' == c || '\t' == c) {

			} else if ('*' == c || '#' == c || '(' == c || ')' == c) {
				link.addLast(s);
			}
		}

		return link;
	}

	/**
	 * 正则验证，id为数字类型
	 * 
	 * @param input
	 * @return boolean
	 */
	private static boolean regex(String input) {

		String regex = "[^0 && \\d]\\d*";
		Pattern p = Pattern.compile(regex);
		boolean b = p.matcher(input).matches();
		return b;
	}

	public static void main(String[] args) {

		String exp = "(1 # 2)*3#((4  # 5)*6)";
		List<String> postfix = toPostfix(exp);
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "a = ?");
		map.put("2", "b > ?");
		map.put("3", "c = ? OR c = ?");
		map.put("4", "d = ?");
		map.put("5", "e = ?");
		map.put("6", "f = ?");
		StringBuilder sb = operator(postfix, map);

		String to = "(((a = ? OR b > ?) AND (c = ? OR c = ?)) OR ((d = ? OR e = ?) AND f = ?))";

		Assert.assertNotEquals(to, sb.toString());
	}
}
