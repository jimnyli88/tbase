package cn.com.ut.core.dal.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface SpringJdbcOperationsApi {

	int update(JdbcTemplate jdbcTemplate, String sql, Object... args);

	int[] batchUpdate(JdbcTemplate jdbcTemplate, String sql, List<Object[]> batchArgs);

	<T> T queryForObject(JdbcTemplate jdbcTemplate, String sql, Class<T> requiredType,
			Object... args);

	<T> T find(JdbcTemplate jdbcTemplate, String sql, Class<T> requiredType, Object... args);

	<T> List<T> queryForList(JdbcTemplate jdbcTemplate, String sql, Class<T> elementType,
			Object... args);

	<T> List<T> query(JdbcTemplate jdbcTemplate, String sql, Class<T> requiredType, Object... args);

	long queryForLong(JdbcTemplate jdbcTemplate, String sql, Object... args);

	int queryForInt(JdbcTemplate jdbcTemplate, String sql, Object... args);

	List<Map<String, Object>> queryForList(JdbcTemplate jdbcTemplate, String sql, Object... args);

	Map<String, Object> queryForMap(JdbcTemplate jdbcTemplate, String sql, Object... args);

	Map<String, Object> queryForMap(boolean throwException, JdbcTemplate jdbcTemplate, String sql,
			Object... args);

}