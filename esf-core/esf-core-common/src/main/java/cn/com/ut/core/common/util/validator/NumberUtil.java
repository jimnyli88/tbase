package cn.com.ut.core.common.util.validator;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.util.CommonUtil;

/**
 * 数字格式及正则校验
 * 
 * @author wuxiaohua
 * @since 2015-3-19
 */
public class NumberUtil {

	private static final Logger logger = LoggerFactory.getLogger(NumberUtil.class);

	@Test
	public void test() {

		assertTrue(checkMobileNumber("17035185940"));
	}

	/**
	 * 验证Cgid，需满足总长32位，前8位为厂家生产许可号mid，后24位为产品序号pid,除了长度，mid和pid只能是[a-zA-Z0-9]和
	 * "-"，但开头和结尾不能是"-"，且不允许"-"连续出现
	 * 
	 * @param length
	 * @param value
	 * @return 是否为合法cgid
	 */
	public static boolean checkCgid(int length, String value) {

		boolean result = true;

		if (value == null || !value.matches("[a-zA-Z0-9-]{" + length + "}")) {
			return false;
		}

		if (value.startsWith("-") || value.endsWith("-") || value.indexOf("--") != -1) {
			System.out.println("==");
			result = false;
		}

		return result;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param value
	 * @return 是否为合法邮箱
	 */
	public static boolean checkEmail(String value) {

		if (value == null)
			return false;

		boolean result = true;

		String format = "^([a-zA-Z0-9]*[-_\\.]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		format = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
		Pattern regex = Pattern.compile(format);

		Matcher matcher = regex.matcher(value);

		result = matcher.matches();

		return result;
	}

	/**
	 * 验证邮政编码
	 * 
	 * @param value
	 * @return 是否为合法邮政编码
	 */
	public static boolean checkPost(String value) {

		if (value == null)
			return false;

		if (value.matches("[1-9]\\d{5}(?!\\d)")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证固定电话
	 * 
	 * @param value
	 * @return 是否为合法固话
	 */
	public static boolean checkPhone(String value) {

		if (value == null)
			return false;

		if (value.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机
	 * 
	 * @param value
	 * @return 是否为合法手机号
	 */
	public static boolean checkMobileNumber(String value) {

		if (value == null)
			return false;

		boolean result = true;

		String format = "^1(3[0-9]|4[0-9]|5[0-9]|7[0-9]|8[0-9])\\d{8}$";
		// 过时："^((13\\d)|(15[0-3[5-9]])|(18[0[5-9]]))\\d{8}$";

		Pattern regex = Pattern.compile(format);

		Matcher matcher = regex.matcher(value);

		result = matcher.matches();

		return result;
	}

	/**
	 * 密码验证，字母、数字或符号的组合
	 * 
	 * @param value
	 * @return 是否为合法密码
	 */
	public static boolean checkUserPassword(String value) {

		if (value == null)
			return false;

		String regex = "[\\p{Lower}|\\p{Upper}|\\p{Punct}|\\p{Digit}]{6,20}";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(value);
		boolean b = matcher.matches();
		return b;
	}

	/**
	 * 验证用户名，支持英文、数字及"-"、"_"组合，长度6-20
	 * 
	 * @param value
	 * @return 是否为合法用户名
	 */
	public static boolean checkUserAccount(String value) {

		if (value == null)
			return false;
		String regex = "[\\w-]{3,20}";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(value);
		boolean b = matcher.matches();
		return b;
	}

	/**
	 * 验证值的范围
	 * 
	 * @param value
	 * @param list
	 * @return 是否为合法数值
	 */
	public static boolean checkValueRange(String value, List<Object> list) {

		if (value == null || list == null)
			return false;

		return list.contains(value);
	}

	/**
	 * 验证值的范围
	 * 
	 * @param value
	 *            值
	 * @param array
	 *            范围
	 * @return 是否为合法
	 */
	public static boolean checkValueRange(String value, Object... array) {

		if (value == null || array == null)
			return false;

		for (Object e : array) {
			if (value.equals(e))
				return true;
		}

		return false;
	}

	/**
	 * 验证长度范围
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return 是否为合法长度
	 */
	public static boolean checkLength(String value, int min, int max) {

		int len = value == null ? 0 : value.trim().length();

		if (min != -1 && len < min)
			return false;

		if (max != -1 && len > max)
			return false;

		return true;
	}

	/**
	 * 验证IP
	 * 
	 * @param value
	 * @return 是否为合法ip
	 */
	public static boolean checkIPAddress(String value) {

		boolean result = true;
		// \b表示单词边界
		String format = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

		Pattern regex = Pattern.compile(format);

		Matcher matcher = regex.matcher(value);

		result = matcher.matches();

		return result;
	}

	/**
	 * 根据输入账号判断注册的账号类型
	 * 
	 * @param account
	 * @return 是否为合法账号
	 */
	public static int confirmAccountType(String account) {

		if (NumberUtil.checkEmail(account))
			return ConstantUtil.ACCOUNT_TYPE_EMAIL;

		if (NumberUtil.checkMobileNumber(account))
			return ConstantUtil.ACCOUNT_TYPE_MOBILE;

		return ConstantUtil.ACCOUNT_TYPE_USERNAME;

	}

	/**
	 * 整数验证
	 * 
	 * @param value
	 * @return 是否为合法整数
	 */
	public static boolean isInteger(String value) {

		if (value == null)
			return false;
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 整数验证
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return 是否为合法整数
	 */
	public static boolean isInteger(String value, Integer min, Integer max) {

		if (!isInteger(value)) {
			return false;
		}

		int i = Integer.parseInt(value);
		if (min != null) {
			if (i < min)
				return false;
		}

		if (max != null) {
			if (i > max)
				return false;
		}
		return true;

	}

	/**
	 * 整数验证
	 * 
	 * @param value
	 * @param min
	 * @return 是否为合法整数
	 */
	public static boolean isInteger(String value, Integer min) {

		return isInteger(value, min, null);

	}

	/**
	 * 长整型验证
	 * 
	 * @param value
	 * @return 是否为合法长整形数
	 */
	public static boolean isLong(String value) {

		if (value == null)
			return false;
		try {
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Float验证
	 * 
	 * @param value
	 * @return 是否为合法浮点数
	 */
	public static boolean isFloat(String value) {

		if (value == null)
			return false;

		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Double验证
	 * 
	 * @param value
	 * @return 是否double值
	 */
	public static boolean isDouble(String value) {

		if (value == null)
			return false;

		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 首位可以出现负号，负号最多出现一次； 整数部分，首位如果以0开头，只能出现在个位，首位非0开头，则后面可以接的数字位数：maxLength-1；
	 * 无小数部分
	 * 
	 * @param integer
	 *            输入值
	 * @param negative
	 *            是否可以为负数
	 * @param maxLength
	 *            最大允许长度
	 * @return 是否为合法整数
	 */
	public static boolean isInteger(String integer, boolean negative, int maxLength) {

		boolean result = false;
		if (CommonUtil.isEmpty(integer))
			return result;

		String formatIntOne = "[\\d&&[^0]]\\d{0," + (maxLength - 1) + "}";
		String formatIntTwo = "0";
		if (negative) {
			formatIntOne = "-{0,1}" + formatIntOne;
			formatIntTwo = "-{0,1}" + formatIntTwo;
		}

		Pattern regex = Pattern.compile(formatIntOne);
		Matcher matcher = regex.matcher(integer);
		boolean bIntOne = matcher.matches();

		regex = Pattern.compile(formatIntTwo);
		matcher = regex.matcher(integer);
		boolean bIntTwo = matcher.matches();

		logger.debug("formatIntOne==" + bIntOne + "\nformatIntTwo==" + bIntTwo);

		result = bIntOne | bIntTwo;

		if (result) {
			try {
				Integer.parseInt(integer);
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return result;
	}

	/**
	 * 首位可以出现负号，负号最多出现一次；
	 * 整数部分，首位如果以0开头，只能出现在个位，首位非0开头，则后面可以接的数字位数：maxLength-maxDigits-1；
	 * 小数部分，如果没有，小数点不出现，如果有小数点，后面至少有一位数字，小数点后数字位数不超过maxDigits；
	 * 
	 * @param numerical
	 *            输入值
	 * @param negative
	 *            是否可以为负数
	 * @param maxLength
	 *            最大允许长度，含小数精度位数
	 * @param maxDigits
	 *            小数精度位数
	 * @return 是否为合法实数
	 */
	public static boolean isDecimal(String numerical, boolean negative, int maxLength,
			int maxDigits) {

		boolean result = false;
		if (CommonUtil.isEmpty(numerical))
			return result;

		String formatIntOne = "[\\d&&[^0]]\\d{0," + (maxLength - maxDigits - 1) + "}";
		String formatIntTwo = "0";
		if (negative) {
			formatIntOne = "-{0,1}" + formatIntOne;
			formatIntTwo = "-{0,1}" + formatIntTwo;
		}

		String formatDecimalOne = formatIntOne + ".\\d{1," + maxDigits + "}";
		String formatDecimalTwo = formatIntTwo + ".\\d{1," + maxDigits + "}";

		Pattern regex = Pattern.compile(formatIntOne);
		Matcher matcher = regex.matcher(numerical);
		boolean bIntOne = matcher.matches();

		regex = Pattern.compile(formatIntTwo);
		matcher = regex.matcher(numerical);
		boolean bIntTwo = matcher.matches();

		regex = Pattern.compile(formatDecimalOne);
		matcher = regex.matcher(numerical);
		boolean bDecimalOne = matcher.matches();

		regex = Pattern.compile(formatDecimalTwo);
		matcher = regex.matcher(numerical);
		boolean bDecimalTwo = matcher.matches();

		logger.debug("formatIntOne==" + bIntOne + "\nformatIntTwo==" + bIntTwo
				+ "\nformatDecimalOne==" + bDecimalOne + "\nformatDecimalTwo" + bDecimalTwo);

		if (bIntOne || bIntTwo || bDecimalOne || bDecimalOne || bDecimalTwo)
			result = true;

		if (result) {
			try {
				new BigDecimal(numerical);
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return result;
	}

}
