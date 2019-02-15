package cn.com.ut.core.common.util.converter;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.validator.NumberUtil;

/**
 * 时间工具类
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class DateTimeUtil {

	/**
	 * 每个月份的天数，二月份闰年比平年多一天
	 */
	private static final int[] MONTH_DAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 *
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp currentDateTime() {

		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * 延长时间
	 * 
	 * @param time
	 * @param number
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp addLong(java.sql.Timestamp time, long number) {

		if (time == null)
			return time;
		return new java.sql.Timestamp(time.getTime() + number);
	}

	/**
	 * 将字符串指定的时间以指定格式格式化
	 * 
	 * @param date
	 * @param format
	 * @return java.sql.Date or null
	 */
	public static java.sql.Date dateFormat(String date, String format) {

		if (CommonUtil.isEmpty(date))
			return null;
		if (CommonUtil.isEmpty(format))
			format = ConstantUtil.DATE_FORMAT;
		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			result = sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return new java.sql.Date(result.getTime());
	}

	/**
	 * 将字符串指定的时间以指定格式格式化
	 * 
	 * @param datetime
	 * @param format
	 * @return java.sql.Timestamp or null
	 */
	public static java.sql.Timestamp dateTimeFormat(String datetime, String format) {

		if (CommonUtil.isEmpty(datetime))
			return null;
		if (CommonUtil.isEmpty(format))
			format = ConstantUtil.DATETIME_FORMAT;
		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			result = sdf.parse(datetime);
		} catch (ParseException e) {
			return null;
		}
		return new java.sql.Timestamp(result.getTime());
	}

	/**
	 * 解析时间
	 * 
	 * @param datetime
	 * @param format
	 * @return java.util.Date or null
	 */
	public static java.util.Date parseDate(String datetime, String format) {

		if (CommonUtil.isEmpty(datetime))
			return null;
		if (CommonUtil.isEmpty(format))
			format = ConstantUtil.DATETIME_FORMAT;
		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			result = sdf.parse(datetime);
		} catch (ParseException e) {
			return null;
		}
		return result;
	}

	/**
	 * 时间转字符串
	 * 
	 * @param date
	 * @param format
	 * @return date string or null
	 */
	public static String dateToString(java.util.Date date, String format) {

		if (date == null)
			return null;

		if (CommonUtil.isEmpty(format))
			format = ConstantUtil.DATETIME_FORMAT;

		String result = null;
		try {
			result = new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 对指定的时间延长指定分钟
	 * 
	 * @param date
	 * @param minutes
	 * @return java.util.Date
	 */
	public static java.util.Date addMinutes(java.util.Date date, int minutes) {

		if (date == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	/**
	 * 对指定的时间延长指定秒数
	 * 
	 * @param date
	 * @param seconds
	 * @return java.util.Date
	 */
	public static java.util.Date addSeconds(java.util.Date date, int seconds) {

		if (date == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * 对指定的时间延长指定天数
	 * 
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static java.util.Date addDays(java.util.Date date, int days) {

		if (date == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 * 修改时间的年数
	 * 
	 * @param date
	 *            时间
	 * @param years
	 *            年数
	 * @return java.util.Date
	 */
	public static java.util.Date addYears(java.util.Date date, int years) {

		if (date == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}

	/**
	 * 对指定的时间延长指定月数
	 * 
	 * @param date
	 * @param months
	 * @param clear
	 *            是否修改传入时间返回时间
	 * @return java.util.Date
	 */
	public static java.util.Date addMonths(java.util.Date date, int months, boolean clear) {

		if (date == null) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		if (clear) {
			cal.clear();
			cal.set(year, month, day);
		}
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * 转换"n秒"间隔为"小时分秒"表示
	 * 
	 * @param interval
	 * @return 指定描述转换为时分秒格式的字符串
	 */
	public static String secondsIntervalToHHmmssZh(long interval) {

		if (interval < 0)
			return "";

		long h = interval / (60L * 60L);
		long hr = interval % (60L * 60L);
		long m = hr / 60L;
		long mr = hr % 60L;

		StringBuilder sb = new StringBuilder();

		if (h > 0)
			sb.append(h).append("小时");

		if (sb.length() > 0) {
			sb.append(",");
			sb.append(m).append("分");
		} else {
			if (m > 0) {
				sb.append(m).append("分");
			}
		}

		if (sb.length() > 0) {
			sb.append(",");
			sb.append(mr).append("秒");
		} else {
			sb.append(mr).append("秒");
		}

		return sb.toString();
	}

	/**
	 * 获取指定时间的前一天
	 * 
	 * @param nowTime
	 * @return 指定时间的前一天
	 */
	public static Timestamp getPreTime(Timestamp nowTime) {

		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(nowTime);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		Timestamp preTime = new Timestamp(calendar.getTime().getTime()); // 得到前一天的时间
		String format = "yyyy-MM-dd";
		String beforeTime = DateTimeUtil.dateToString(preTime, null);
		preTime = DateTimeUtil.dateTimeFormat(beforeTime, format);// 2014-08-27
																	// 00:00:00.0
		return preTime;

	}

	/**
	 * 获取指定时间的后一天
	 * 
	 * @param nowTime
	 * @return 指定时间的后一天
	 */
	public static Timestamp addOneDay(Timestamp nowTime) {

		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(nowTime);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为前一天
		Timestamp preTime = new Timestamp(calendar.getTime().getTime()); // 得到前一天的时间
		String format = "yyyy-MM-dd";
		String beforeTime = DateTimeUtil.dateToString(preTime, null);
		preTime = DateTimeUtil.dateTimeFormat(beforeTime, format);// 2014-08-27
																	// 00:00:00.0
		return preTime;

	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * 
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String str1, String str2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

	/**
	 * 计算指定时间到另一时间剩余？天？时？分？秒
	 * 
	 * @param maxConfirmTime
	 * @param currTime
	 * @return string or null
	 */
	public static String getDistanceTime(Timestamp maxConfirmTime, Timestamp currTime) {

		if (maxConfirmTime == null || currTime == null) {
			return null;
		}
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long time1 = maxConfirmTime.getTime();
		long time2 = currTime.getTime();
		long diff;
		if (time1 < time2) {
			// diff = time2 - time1;
			return null;
		} else {
			diff = time1 - time2;
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		return day + "天" + hour + "时" + min + "分" + sec + "秒";

	}

	/**
	 *
	 * @return java.sql.Time
	 */
	public static Time currentTime() {

		return Time.valueOf(DateTimeUtil.dateToString(DateTimeUtil.currentDateTime(),
				ConstantUtil.TIME_FORMAT));

	}

	/**
	 * 时间格式化
	 * 
	 * @param appointTime
	 * @param timeFormat
	 * @return java.sql.Time or null
	 */
	public static java.sql.Time timeFormat(String appointTime, String timeFormat) {

		if (CommonUtil.isEmpty(appointTime))
			return null;
		if (CommonUtil.isEmpty(timeFormat))
			timeFormat = ConstantUtil.TIME_FORMAT;

		return Time.valueOf(DateTimeUtil
				.dateToString(DateTimeUtil.dateTimeFormat(appointTime, null), timeFormat));

	}

	/**
	 * java.util.Date转换为java.sql.Time，仅保留时间段，截掉日期段，将日期段置为1970年1月1日即可
	 * 
	 * @param date
	 * @return java.sql.Time
	 */
	public static java.sql.Time dateToSqlTime(java.util.Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.clear(Calendar.YEAR);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new java.sql.Time(cal.getTimeInMillis());
	}

	/**
	 * 是否为闰年
	 * 
	 * @param year
	 * @return if is leap year ,then return true
	 */
	public static boolean isLeapYear(int year) {

		if (year % 100 == 0) {
			return (year % 400 == 0);
		} else {
			return (year % 4 == 0);
		}
	}

	/**
	 * 正则判断是否为日期格式，并验证日期的合法范围
	 * 
	 * @param date
	 * @param dateFormat
	 * @return 是否合法日期
	 */
	public static boolean isDateFormat(String date, String dateFormat) {

		boolean result = false;
		String format = null;
		if (ConstantUtil.DATE_FORMAT.equals(dateFormat)) {

			format = "\\d{4}-\\d{2}-\\d{2}";
			Pattern regex = Pattern.compile(format);
			Matcher matcher = regex.matcher(date);
			if (matcher.matches()) {
				String[] array = date.split("-");
				int year = Integer.parseInt(array[0]);
				int month = Integer.parseInt(array[1]);
				int day = Integer.parseInt(array[2]);
				result = isValidDate(year, month, day);
			}
		} else if (ConstantUtil.DATE_SHORT_FORMAT.equals(dateFormat)) {

			format = "\\d{8}";
			Pattern regex = Pattern.compile(format);
			Matcher matcher = regex.matcher(date);
			if (matcher.matches()) {
				int year = Integer.parseInt(date.substring(0, 4));
				int month = Integer.parseInt(date.substring(4, 6));
				int day = Integer.parseInt(date.substring(6));
				result = isValidDate(year, month, day);
			}

		} else {
		}
		return result;
	}

	/**
	 * 正则判断是否为时间格式，并验证时间的合法范围
	 * 
	 * @param time
	 * @param timeFormat
	 * @return 是否为指定的时间格式
	 */
	public static boolean isTimeFormat(String time, String timeFormat) {

		boolean result = false;
		String format = null;
		if (ConstantUtil.TIME_FORMAT.equals(timeFormat)) {

			format = "\\d{2}:\\d{2}:\\d{2}";
			Pattern regex = Pattern.compile(format);
			Matcher matcher = regex.matcher(time);
			if (matcher.matches()) {
				String[] timeArray = time.split(":");
				int hour = Integer.parseInt(timeArray[0]);
				int minute = Integer.parseInt(timeArray[1]);
				int second = Integer.parseInt(timeArray[2]);
				int millisecond = 0;
				result = isValidTime(hour, minute, second, millisecond);
			}
		} else if (ConstantUtil.TIME_SHORT_FORMAT.equals(timeFormat)) {

			format = "\\d{6}";
			Pattern regex = Pattern.compile(format);
			Matcher matcher = regex.matcher(time);
			if (matcher.matches()) {
				int hour = Integer.parseInt(time.substring(0, 2));
				int minute = Integer.parseInt(time.substring(2, 4));
				int second = Integer.parseInt(time.substring(4));
				int millisecond = 0;
				result = isValidTime(hour, minute, second, millisecond);
			}
		}
		return result;
	}

	/**
	 * 正则判断是否为日期时间格式，并验证日期和时间的合法范围
	 * 
	 * @param dateTime
	 * @param dateTimeFormat
	 * @return 是否为指定的时间格式
	 */
	public static boolean isDateTimeFormat(String dateTime, String dateTimeFormat) {

		boolean result = false;
		String format = null;

		if (ConstantUtil.DATETIME_FORMAT.equals(dateTimeFormat)) {

			format = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
			Pattern regex = Pattern.compile(format);
			Matcher matcher = regex.matcher(dateTime);
			if (matcher.matches()) {
				String[] dateTimeArray = dateTime.split(" ");
				result = isDateFormat(dateTimeArray[0], ConstantUtil.DATE_FORMAT);
				result &= isTimeFormat(dateTimeArray[1], ConstantUtil.TIME_FORMAT);
			}
		}
		if (ConstantUtil.DATETIME_SHORT_FORMAT.equals(dateTimeFormat)) {

			format = "\\d{14}";
			Pattern regex = Pattern.compile(format);
			Matcher matcher = regex.matcher(dateTime);
			if (matcher.matches()) {
				result = isDateFormat(dateTime.substring(0, 8), ConstantUtil.DATE_SHORT_FORMAT);
				result &= isTimeFormat(dateTime.substring(8), ConstantUtil.TIME_SHORT_FORMAT);
			}
		} else {
		}
		return result;
	}

	/**
	 * 验证指定的年月日是否合法日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return boolean value
	 */
	private static boolean isValidDate(int year, int month, int day) {

		boolean result = (year >= 1970 && year <= 9999);
		if (result) {
			result = (month >= 1 && month <= 12);
			if (result) {
				if (isLeapYear(year) && month == 2) {
					result = (day >= 1 && day <= 29);
				} else {
					result = (day >= 1 && day <= MONTH_DAYS[month - 1]);
				}
			}
		}
		return result;

	}

	/**
	 * 验证指定的时分秒是否合法
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @param millisecond
	 * @return boolean value
	 */
	private static boolean isValidTime(int hour, int minute, int second, int millisecond) {

		boolean result = (hour >= 0 && hour <= 23);
		result &= (minute >= 0 && minute <= 59);
		result &= (second >= 0 && second <= 59);
		result &= (millisecond >= 0 && millisecond <= 999);
		return result;
	}

	/**
	 * 获取当月的第一天
	 */
	public static java.sql.Timestamp getFirstdateOfMonth() {

		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		// cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);
		// 将小时至0
		cal_1.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		cal_1.set(Calendar.MINUTE, 0);
		// 将秒至0
		cal_1.set(Calendar.SECOND, 0);
		// 毫秒
		cal_1.set(Calendar.MILLISECOND, 0);
		return new java.sql.Timestamp(cal_1.getTimeInMillis());
	}

	/**
	 * 获取当周的第一天
	 */
	public static java.sql.Timestamp getFirstdateOfWeek() {

		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		// cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 将小时至0
		cal_1.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		cal_1.set(Calendar.MINUTE, 0);
		// 将秒至0
		cal_1.set(Calendar.SECOND, 0);
		// 毫秒
		cal_1.set(Calendar.MILLISECOND, 0);
		return new java.sql.Timestamp(cal_1.getTimeInMillis());
	}

	/**
	 * 获取当天零点
	 */
	public static java.sql.Timestamp getFirstdateZero() {

		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		// 将小时至0
		cal_1.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		cal_1.set(Calendar.MINUTE, 0);
		// 将秒至0
		cal_1.set(Calendar.SECOND, 0);
		// 毫秒
		cal_1.set(Calendar.MILLISECOND, 0);
		return new java.sql.Timestamp(cal_1.getTimeInMillis());
	}

	/**
	 * 获取当天零点
	 */
	public static java.sql.Timestamp getFirstdateOfYear() {

		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.set(Calendar.DAY_OF_YEAR, 1);
		// 将小时至0
		cal_1.set(Calendar.HOUR_OF_DAY, 0);
		// 将分钟至0
		cal_1.set(Calendar.MINUTE, 0);
		// 将秒至0
		cal_1.set(Calendar.SECOND, 0);
		// 毫秒
		cal_1.set(Calendar.MILLISECOND, 0);
		return new java.sql.Timestamp(cal_1.getTimeInMillis());
	}

	/**
	 * 获取当前时间的值 比如现在10:00:00 返回100000 12:02:59 则返回 120259
	 * 
	 * @return
	 */
	public static Integer getTimeInt() {

		String tempTimeString = dateToString(currentDateTime(), ConstantUtil.TIME_SHORT_FORMAT);
		return Integer.valueOf(tempTimeString);
	}

	/**
	 * 根据时间获取值 比如现在10:00:00 返回100000
	 * 
	 * @param time
	 * @return
	 */
	public static Integer getTimeInt(Time time) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		return calendar.get(Calendar.HOUR_OF_DAY) * 10000 + calendar.get(Calendar.MINUTE) * 100
				+ calendar.get(Calendar.SECOND);
	}

	/**
	 * 把指定的系统配置时间添加到传入时间
	 * 
	 * @param date
	 *            传入时间
	 * @param value
	 *            数字加单位，如7d表示7天。单位:年 y，月份 M， 天 d， 小时 h，分钟 m，秒 s
	 * @return 添加后的新的时间对象，不改变参数时间date
	 */
	public static Date addDateByString(Date date, String value) {

		String str = value.substring(0, value.length() - 1);
		if (!NumberUtil.isInteger(str)) {
			throw new IllegalArgumentException("传入参数value不合法");
		}
		int num = Integer.parseInt(str);
		Date addedDate = new Date(date.getTime());
		String unit = value.substring(value.length() - 1);
		switch (unit) {
		case "y":
			addedDate = DateTimeUtil.addYears(date, num);
			break;
		case "M":
			addedDate = DateTimeUtil.addMonths(date, num, false);
			break;
		case "d":
			addedDate = DateTimeUtil.addDays(date, num);
			break;
		case "h":
			addedDate = DateTimeUtil.addMinutes(date, num * 60);
			break;
		case "m":
			addedDate = DateTimeUtil.addMinutes(date, num);
			break;
		case "s":
			addedDate = DateTimeUtil.addSeconds(date, num);
			break;
		default:
			throw new IllegalArgumentException("传入参数value不合法");
		}
		return addedDate;
	}

	/**
	 * 获取精确到秒的时间戳
	 * 
	 * @param date
	 * @return
	 */
	public static int getSecondTimestamp(Date date) {

		if (null == date) {
			return 0;
		}
		return (int) (date.getTime() / 1000);
	}
}
