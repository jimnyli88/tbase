package cn.com.ut.core.dal.jdbc;

/**
 * spring jdbc操作的sql语句及参数的日志输出辅助类
 * 
 * @author wuxiaohua
 * @since 2015-12-22
 */
public interface SQLExecuteLog {

	/**
	 * jdbc操作的sql语句及参数的日志输出
	 * 
	 * @param sql
	 *            语句
	 * @param array
	 *            参数
	 */
	public void dataAccessLog(String sql, Object[] array);
}
