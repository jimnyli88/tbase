package cn.com.ut.core.dal.jdbc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.ut.core.dal.aop.DALMethodInterceptor;
import cn.com.ut.core.dal.beans.DAORoute;
import cn.com.ut.core.dal.beans.ServiceRoute;
import lombok.extern.slf4j.Slf4j;

/**
 * DAO数据访问对象基类
 * 
 * @author wuxiaohua
 * @since 2017年8月1日
 */
@Slf4j
public class SpringJdbcOperations implements SpringJdbcOperationsApi {

	private static final String MASTER_ROUTE = "/*!mycat:db_type=master*/";

	@Resource
	private SQLExecuteLog sqlExecuteLog;

	/**
	 * 可执行插入、更新和删除语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @param args
	 *            参数值列表
	 * @return 影响的数据记录条数
	 */
	@Override
	public int update(JdbcTemplate jdbcTemplate, String sql, Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		return jdbcTemplate.update(mycatRuleHandle(sql, jdbcTemplate), args);
	}

	/**
	 * 插入、更新和删除语句的批处理操作
	 * 
	 * @param sql
	 *            批处理执行的同一SQL语句，可以是插入、更新和删除语句
	 * @param batchArgs
	 *            参数数组集合，集合中的每个数组应用于每次执行SQL语句依赖的参数值列表
	 * @return 批处理中每次执行SQL语句后，影响的数据记录条数
	 */
	@Override
	public int[] batchUpdate(JdbcTemplate jdbcTemplate, String sql, List<Object[]> batchArgs) {

		sqlExecuteLog.dataAccessLog(sql, null);
		return jdbcTemplate.batchUpdate(mycatRuleHandle(sql, jdbcTemplate), batchArgs);
	}

	/**
	 * 执行查询语句，返回单条数据的单列值，返回值默认为Object实例，根据泛型类型参数，转换为泛型类型的实例
	 * 
	 * @param sql
	 *            SQL语句，用于查询操作
	 * @param requiredType
	 *            泛型类型的Class字节码
	 * @param args
	 *            参数值列表
	 * @return 泛型类型的实例
	 */
	@Override
	public <T> T queryForObject(JdbcTemplate jdbcTemplate, String sql, Class<T> requiredType,
			Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		T t = null;
		try {
			t = jdbcTemplate.queryForObject(mycatRuleHandle(sql, jdbcTemplate), requiredType, args);
		} catch (DataAccessException e) {
			log.info(e.getMessage());
		}
		return t;
	}

	/**
	 * 执行查询语句，返回单条数据的多列值，根据泛型类型参数，返回的各列值即多个Object实例， 对应泛型类型实例中与各列名称相同的成员属性的值
	 * 
	 * @param sql
	 * @param requiredType
	 *            泛型类型
	 * @param args
	 *            参数值列表
	 * @return 泛型类型的实例
	 */
	@Override
	public <T> T find(JdbcTemplate jdbcTemplate, String sql, Class<T> requiredType,
			Object... args) {

		if (requiredType == null)
			return (T) queryForMap(jdbcTemplate, sql, args);

		sqlExecuteLog.dataAccessLog(sql, args);
		T t = null;
		try {
			t = jdbcTemplate.queryForObject(mycatRuleHandle(sql, jdbcTemplate),
					BeanPropertyRowMapper.newInstance(requiredType), args);
		} catch (EmptyResultDataAccessException e) {
			log.info(e.getMessage());
		} catch (IncorrectResultSizeDataAccessException e) {
			log.info(e.getMessage());
		} catch (DataAccessException e) {
			log.info(e.getMessage());
			throw e;
		}
		return t;
	}

	/**
	 * 执行查询语句，返回多条数据的单列值，根据泛型类型参数，返回的每条记录中的单列值将从Object类型转换为泛型类型
	 * 
	 * @param sql
	 *            查询语句
	 * @param elementType
	 *            泛型类型
	 * @param args
	 *            参数值列表
	 * @return 泛型类型的集合
	 */
	@Override
	public <T> List<T> queryForList(JdbcTemplate jdbcTemplate, String sql, Class<T> elementType,
			Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		return jdbcTemplate.queryForList(mycatRuleHandle(sql, jdbcTemplate), elementType, args);
	}

	/**
	 * 执行查询语句，返回多条记录的多列值，根据泛型类型参数，返回的每条记录中多列值，转换为泛型类型实例中对应的属性值
	 * 
	 * @param sql
	 *            查询语句
	 * @param requiredType
	 *            泛型类型
	 * @param args
	 *            参数值列表
	 * @return 泛型类型的集合
	 */
	@Override
	public <T> List<T> query(JdbcTemplate jdbcTemplate, String sql, Class<T> requiredType,
			Object... args) {

		if (requiredType == null)
			return (List<T>) queryForList(jdbcTemplate, sql, args);
		sqlExecuteLog.dataAccessLog(sql, args);
		return jdbcTemplate.query(mycatRuleHandle(sql, jdbcTemplate),
				BeanPropertyRowMapper.newInstance(requiredType), args);
	}

	/**
	 * 执行count查询语句，返回计数
	 * 
	 * @param sql
	 *            查询语句
	 * @param args
	 *            参数值列表
	 * @return 计数
	 */
	@Override
	public long queryForLong(JdbcTemplate jdbcTemplate, String sql, Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		return jdbcTemplate.queryForObject(mycatRuleHandle(sql, jdbcTemplate), args, Long.class);
	}

	/**
	 * 执行count查询语句，返回计数
	 * 
	 * @param sql
	 *            查询语句
	 * @param args
	 *            参数值列表
	 * @return 计数
	 */
	@Override
	public int queryForInt(JdbcTemplate jdbcTemplate, String sql, Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		return jdbcTemplate.queryForObject(mycatRuleHandle(sql, jdbcTemplate), args, Integer.class);
	}

	/**
	 * 执行查询语句，可返回多条记录的多列值，每条记录将转换为Map类型的实例，其中各字段名对应Map中的Key，各字段值对应Map中的Value
	 * 
	 * @param sql
	 *            查询语句
	 * @param args
	 *            参数值列表
	 * @return Map类型的集合
	 */
	@Override
	public List<Map<String, Object>> queryForList(JdbcTemplate jdbcTemplate, String sql,
			Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		return jdbcTemplate.queryForList(mycatRuleHandle(sql, jdbcTemplate), args);
	}

	/**
	 * 执行查询语句，返回单条记录的多列值。该条记录转换为Map类型的实例，其中各字段名对应Map中的Key，各字段值对应Map中的Value
	 * 
	 * @param throwException
	 *            是否抛出异常
	 * @param sql
	 *            查询语句
	 * @param args
	 *            参数值列表
	 * @return Map类型的实例
	 */
	@Override
	public Map<String, Object> queryForMap(boolean throwException, JdbcTemplate jdbcTemplate,
			String sql, Object... args) {

		sqlExecuteLog.dataAccessLog(sql, args);
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(mycatRuleHandle(sql, jdbcTemplate), args);
		} catch (EmptyResultDataAccessException e) {
			log.info(e.getMessage());
			if (throwException)
				throw e;
		} catch (IncorrectResultSizeDataAccessException e) {
			log.info(e.getMessage());
			if (throwException)
				throw e;
		} catch (DataAccessException e) {
			log.info(e.getMessage());
			throw e;
		}
		return map;
	}

	/**
	 * @see SpringJdbcOperations#queryForMap(boolean, JdbcTemplate, String,
	 *      Object...)
	 */
	@Override
	public Map<String, Object> queryForMap(JdbcTemplate jdbcTemplate, String sql, Object... args) {

		return queryForMap(false, jdbcTemplate, sql, args);
	}

	/**
	 * mycat规则处理，动态修改sql，加上路由作为前缀
	 * 
	 * @param sql
	 * @return
	 */
	private String mycatRuleHandle(String sql, JdbcTemplate jdbcTemplate) {

		ServiceRoute serviceRoute = DALMethodInterceptor.getServiceRouteThreadLocal().get();
		DAORoute daoRoute = DALMethodInterceptor.getDaoRouteThreadLocal().get();
		StringBuilder sb = new StringBuilder();
		if (daoRoute.getDataNode() != null) {
			sb.append("/*!mycat:dataNode=").append(daoRoute.getDataNode()).append("*/");
		}
		if (serviceRoute.isMaster()) {
			sb.append(MASTER_ROUTE);
		}
		if (serviceRoute.isXa() && daoRoute.getDaoIndex() == 1) {
			jdbcTemplate.update("SET xa=on");
		}
		return sb.append(sql).toString();
	}
}
