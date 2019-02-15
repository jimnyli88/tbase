package cn.com.ut.core.dal.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.ut.core.cache.CacheHelper;
import cn.com.ut.core.cache.CachedParameter;
import cn.com.ut.core.cache.annotation.Cached;
import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.exception.DAOException;
import cn.com.ut.core.common.jdbc.PageBean;
import cn.com.ut.core.common.util.ArrayUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.converter.DateTimeUtil;
import cn.com.ut.core.dal.beans.ORMapperElement;
import cn.com.ut.core.dal.manager.SchemaMapperManager;
import cn.com.ut.core.dal.sql.SQLHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * DAO数据访问对象基类
 * 
 * @author wuxiaohua
 * @since 2017年8月1日
 */
@Slf4j
public class JdbcOperationsImpl<E extends BaseEntity> extends SpringJdbcOperations
		implements InitializingBean, JdbcOperation<E> {

	/**
	 * 数据表系统字段：主键，创建时间，更新时间，创建人，更新人
	 */
	public static final String[] NAMES = new String[] { BaseEntity.idx, BaseEntity.create_time,
			BaseEntity.update_time, BaseEntity.create_id, BaseEntity.update_id };

	/**
	 * 数据表系统字段：主键，创建时间，更新时间
	 */
	public static final String[] NAMES_ID_CUT = new String[] { BaseEntity.idx,
			BaseEntity.create_time, BaseEntity.update_time };

	/**
	 * 数据表系统字段：主键，创建人，更新人
	 */
	public static final String[] NAMES_ID_CUID = new String[] { BaseEntity.idx,
			BaseEntity.create_id, BaseEntity.update_id };

	/**
	 * 数据表系统字段：更新时间，更新人
	 */
	public static final String[] NAMES_UT_UID = new String[] { BaseEntity.update_time,
			BaseEntity.update_id };

	/**
	 * 数据表系统字段：主键，创建时间，创建人
	 */
	public static final String[] NAMES_ID_CT_CID = new String[] { BaseEntity.idx,
			BaseEntity.create_time, BaseEntity.create_id };

	/**
	 * 基于mycat代理的JdbcTemplate
	 */
	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;

	/**
	 * 数据访问方法缓存参数
	 */
	protected CachedParameter cachedParameter;

	/**
	 * 全局缓存操作辅助类
	 */
	@Autowired(required = false)
	private CacheHelper cacheHelper;

	/**
	 * 数据表所属的逻辑数据库
	 */
	protected String groupName;

	/**
	 * 实体类映射的数据表
	 */
	private String table;

	/**
	 * 实体类映射的配置
	 */
	protected ORMapperElement mapper;

	/**
	 * 实体类的Class
	 */
	protected Class<E> entity;

	/**
	 * 给表字段名加上表别名作为前缀
	 * 
	 * @param prefix
	 *            表别名作为前缀
	 * 
	 * @param column
	 *            表字段名
	 * @return 表字段名加上表别名作为前缀
	 */
	protected String prefixColumn(String prefix, String column) {

		if (prefix == null)
			return column;
		return prefix + "." + column;
	}

	/**
	 * 由实体类解析查询字段集合
	 * 
	 * @param clazz
	 *            实体类
	 * @return 查询字段集合
	 */
	protected static List<String> columnList(Class<? extends BaseEntity> clazz) {

		List<String> columnList = new ArrayList<String>();
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			if (f.getType().equals(String.class) && Modifier.isPublic(f.getModifiers())
					&& Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
				columnList.add(f.getName());

		}
		columnList.add(BaseEntity.idx);
		return columnList;
	}

	/**
	 * 由实体类解析查询字段数组
	 * 
	 * @param clazz
	 *            实体类
	 * @return 查询字段数组
	 */
	protected static String[] columnArray(Class<? extends BaseEntity> clazz) {

		List<String> columnList = columnList(clazz);
		return columnList.toArray(new String[columnList.size()]);
	}

	/**
	 * 根据泛型参数类型获取实体类类型
	 * 
	 * @return 实体类类型
	 */
	@SuppressWarnings("unchecked")
	public Class<E> getEntity() {

		if (entity != null)
			return entity;

		Type type = this.getClass().getGenericSuperclass();

		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			Type[] types = pt.getActualTypeArguments();
			entity = (Class<E>) types[0];
			return entity;
		} else {
			return null;
		}
	}

	public CachedParameter getCachedParameter() {

		return cachedParameter;
	}

	public void setCachedParameter(CachedParameter cachedParameter) {

		this.cachedParameter = cachedParameter;
	}

	/**
	 * see {@link #getJdbcTemplate(String)}
	 */
	@Override
	public JdbcTemplate getJdbcTemplate() {

		return this.jdbcTemplate;
	}

	protected ORMapperElement getMapper() {

		return mapper;
	}

	/**
	 * 根据实体类获取实体关系映射
	 * 
	 * @param entity
	 *            实体类
	 * @return 实体关系映射
	 */
	protected ORMapperElement getMapper(Class<? extends BaseEntity> entity) {

		return SchemaMapperManager.getMapper(entity.getName());
	}

	protected void setMapper(ORMapperElement mapper) {

		this.mapper = mapper;
	}

	/**
	 * 根据实体类获取逻辑数据源组
	 * 
	 * @param entity
	 *            实体类
	 * @return 逻辑数据源组
	 */
	protected String getGroupName(Class<? extends BaseEntity> entity) {

		ORMapperElement orMapperElement = getMapper(entity);
		if (orMapperElement != null)
			return orMapperElement.getSchema();
		return groupName;
	}

	/**
	 * 获取默认逻辑数据源组
	 * 
	 * @return 逻辑数据源组
	 */
	public String getGroupName() {

		if (getMapper() != null)
			return getMapper().getSchema();
		return groupName;
	}

	protected void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	/**
	 * 获取实体映射的数据表
	 * 
	 * @return 该实体映射的数据表
	 */
	@Override
	public String getTable() {

		if (getMapper() != null)
			return getMapper().getTable();
		return table;
	}

	/**
	 * 获取实体映射的数据表
	 * 
	 * @param entity
	 *            实体类
	 * @return 该实体映射的数据表
	 */
	@Override
	public String getTable(Class<? extends BaseEntity> entity) {

		ORMapperElement orMapperElement = getMapper(entity);
		if (orMapperElement != null)
			return orMapperElement.getTable();
		return table;
	}

	protected void setTable(String table) {

		this.table = table;
	}

	/**
	 * 初始化方法
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		if (getEntity() != null) {
			this.mapper = SchemaMapperManager.getMapper(getEntity().getName());
		}
		this.cachedParameter = new CachedParameter();
		if (getClass().isAnnotationPresent(Cached.class)) {
			this.cachedParameter.setCacheable(true);
		}
	}

	/**
	 * 将Map中指定的一组key对应的value转化为List
	 * 
	 * @param map
	 *            集合
	 * @param keys
	 *            key数组
	 * @return
	 */
	public List<Object> mapValuesToList(Map<String, Object> map, String[] keys) {

		if (map == null || keys == null || keys.length == 0)
			return null;
		List<Object> list = new ArrayList<Object>();
		for (String key : keys) {
			list.add(map.get(key));
		}
		return list;
	}

	/******************************************
	 * Jdbc Operations
	 ******************************************/

	/**
	 * 计数:select count(*) from table where...
	 * 
	 * @param table
	 *            表名称
	 * @param countColumn
	 *            列名称
	 * @param whereColumnArray
	 *            where条件(数组形式),如new String["field1",
	 *            "field2"]将转成sql中where子句的一部分,field1=? and field2=?
	 * @param whereColumnJoin
	 *            where条件(文本形式),如field1=? and (field2=1 or field3=?). 这种方式比较灵活
	 * @param replaceArray
	 *            将sql语句中出现的若干{IN}依次替换成replaceArray[x]个数的IN(?,?,...)形式
	 * @param parameterArray
	 *            与sql语句中?出现次数匹配的对象数组即参数
	 * @return
	 */
	@Override
	public long count(String table, String countColumn, String[] whereColumnArray,
			String whereColumnJoin, int[] replaceArray, Object[] parameterArray) {

		if (table == null)
			table = getTable();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		if (countColumn == null)
			sql.append(" COUNT(*) ");
		else
			sql.append(" COUNT(").append(countColumn).append(") ");
		sql.append(" FROM ").append(table).append(" WHERE TRUE ");

		sql.append(whereCondition(whereColumnArray, whereColumnJoin, replaceArray));
		return queryForLong(getJdbcTemplate(), sql.toString(), parameterArray);
	}

	/**
	 * @see #count(String, String, String[], String, int[], Object[])
	 */
	@Override
	public long count(String table, String countColumn, String[] whereColumnArray,
			Object[] parameterArray) {

		return count(table, countColumn, whereColumnArray, null, null, parameterArray);
	}

	/**
	 * 插入一条记录
	 * 
	 * @param table
	 *            表名称
	 * @param insertColumnArray
	 *            插入字段数组
	 * @param appendColumns
	 *            附加插入字段数组
	 * @param parameterArray
	 *            参数值数组
	 * @param cachedParameter
	 *            缓存参数
	 * @return 插入记录数
	 */
	@Override
	public int add(String table, String[] insertColumnArray, String[] appendColumns,
			Object[] parameterArray, CachedParameter cachedParameter) {

		if (appendColumns != null)
			insertColumnArray = ArrayUtil.joinArray(insertColumnArray, appendColumns);

		if (table == null)
			table = getTable();

		String sql = SQLHelper.insertSQL(table, insertColumnArray).toString();

		int count = update(getJdbcTemplate(), sql, parameterArray);

		// 清除关联缓存
		if (count == 1 && cachedParameter != null && cachedParameter.getEvict() != null) {
			cacheHelper.delete(cachedParameter.getEvict());
		}

		return count;
	}

	/**
	 * 批量插入
	 * 
	 * @param table
	 *            表名称
	 * @param insertColumnArray
	 *            插入字段数组
	 * @param appendColumns
	 *            附加插入字段数组
	 * @param parameterArrayBatch
	 *            参数值数组集合
	 * @return 插入记录数数组
	 */
	@Override
	public int[] addBatch(String table, String[] insertColumnArray, String[] appendColumns,
			List<Object[]> parameterArrayBatch) {

		if (table == null)
			table = getTable();

		if (appendColumns != null)
			insertColumnArray = ArrayUtil.joinArray(insertColumnArray, appendColumns);

		String sql = SQLHelper.insertSQL(table, insertColumnArray).toString();

		int[] counts = batchUpdate(getJdbcTemplate(), sql, parameterArrayBatch);

		return counts;
	}

	/**
	 * 批量插入
	 * 
	 * @param table
	 *            表名称
	 * @param insertColumnArray
	 *            插入字段数组
	 * @param appendColumns
	 *            附加插入字段数组
	 * @param vos
	 *            参数值数组集合
	 * @return 插入记录数数组
	 */
	@Override
	public int[] addVoBatch(String table, String[] insertColumnArray, String[] appendColumns,
			List<Map<String, Object>> vos) {

		if (appendColumns != null)
			insertColumnArray = ArrayUtil.joinArray(insertColumnArray, appendColumns);

		List<Object[]> parameterArrayBatch = new ArrayList<Object[]>(vos.size());
		for (Map<String, Object> vo : vos) {
			List<Object> parameterArray = new ArrayList<Object>();
			for (String insertColumn : insertColumnArray) {
				parameterArray.add(vo.get(insertColumn));
			}
			parameterArrayBatch.add(parameterArray.toArray());
		}

		return addBatch(table, insertColumnArray, null, parameterArrayBatch);
	}

	/**
	 * @see #add(String, String[], String[], Object[], CachedParameter)
	 */
	@Override
	public int add(String table, String[] insertColumnArray, String[] appendColumns,
			Object[] parameterArray) {

		return add(table, insertColumnArray, appendColumns, parameterArray, null);
	}

	/**
	 * 更新操作时不同形式更新字段的合并
	 * 
	 * @param updateColumnArray
	 *            字段数组
	 * @param updateColumnJoin
	 *            多字段拼接形式
	 * @return 多字段拼接形式
	 */
	private StringBuilder setCondition(String[] updateColumnArray, String updateColumnJoin) {

		StringBuilder sql = new StringBuilder();
		if (updateColumnArray != null && updateColumnArray.length > 0) {
			sql.append(" ").append(ArrayUtil.joinArrayElement(updateColumnArray, ", ", null, "=?"))
					.append(" ");
		}

		if (updateColumnJoin != null) {
			if (sql.length() == 0) {
				sql.append(" ").append(updateColumnJoin).append(" ");
			} else {
				sql.append(", ").append(updateColumnJoin).append(" ");
			}
		}

		return sql;
	}

	/**
	 * where子句的构建
	 * 
	 * @param whereColumnArray
	 *            条件字段数组
	 * @param whereColumnJoin
	 *            多条件字段拼接形式
	 * @param replaceArray
	 *            多条件字段拼接形式中每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * @return 多条件字段拼接形式的where子句
	 */
	private StringBuilder whereCondition(String[] whereColumnArray, String whereColumnJoin,
			int[] replaceArray) {

		StringBuilder sql = new StringBuilder();

		whereColumnJoin = replaceInCase(whereColumnJoin, replaceArray);

		if (whereColumnArray != null && whereColumnArray.length > 0) {
			sql.append(" AND ( ")
					.append(ArrayUtil.joinArrayElement(whereColumnArray, " AND ", null, "=?"))
					.append(") ");
		}

		if (whereColumnJoin != null) {
			sql.append(" AND ( ").append(whereColumnJoin).append(" ) ");
		}
		return sql;
	}

	/**
	 * sql子句中的每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * 
	 * @param sql
	 *            语句
	 * @param replaceArray
	 *            占位符数量数组
	 * @return 替换后的语句
	 */
	@Override
	public String replaceInCase(String sql, int[] replaceArray) {

		if (sql != null) {
			if (replaceArray != null && replaceArray.length > 0) {
				for (int replace : replaceArray) {
					sql = sql.replaceFirst("\\{IN\\}",
							"IN (" + ArrayUtil.joinSameElement("?", replace, ",") + ")");
				}
			}

			if (sql.indexOf("{IN}") != -1) {
				// 替换全部"非空白字符(一次或多次)+空白字符(可多空格|制表符|换行符|回车符等)+{IN}"为"TRUE"
				sql = sql.replaceAll("\\S++\\s++\\{IN\\}", "TRUE");
			}
		}
		return sql;
	}

	/**
	 * 按照主键id更新一条记录:update table set... where id=?
	 * 
	 * @see JdbcOperationsImpl#update(String, String[], String[], String,
	 *      String[], String, int[], Object[], Object[], CachedParameter)
	 * 
	 */
	@Override
	public int updateById(String table, String[] updateColumnArray, String[] appendColumns,
			String updateColumnJoin, Object[] updateParameterArray, String id,
			CachedParameter cachedParameter) {

		if (table == null)
			table = getTable();

		int count = update(table, updateColumnArray, appendColumns, updateColumnJoin,
				new String[] { BaseEntity.idx }, null, null, updateParameterArray,
				new Object[] { id }, new CachedParameter());

		if (cachedParameter == null) {
			cachedParameter = getCachedParameter();
		}
		// 清除缓存
		if (count == 1 && (cachedParameter != null && cachedParameter.isCacheable())) {
			cacheHelper.delete(table + "/" + id);

			// 清除关联缓存
			if (cachedParameter != null && cachedParameter.getEvict() != null) {
				cacheHelper.delete(cachedParameter.getEvict());
			}
		}

		return count;
	}

	/**
	 * @see #updateById(String, String[], String[], String, Object[], String,
	 *      CachedParameter)
	 */
	@Override
	public int updateById(String table, String[] updateColumnArray, String[] appendColumns,
			Object[] updateParameterArray, String id) {

		return updateById(table, updateColumnArray, appendColumns, null, updateParameterArray, id,
				null);
	}

	/**
	 * @see #update(String, String[], String[], String, String[], String, int[],
	 *      Object[], Object[], CachedParameter)
	 */
	@Override
	public int update(String table, String[] updateColumnArray, String[] appendColumns,
			String[] whereColumnArray, Object[] updateParameterArray,
			Object[] whereParameterArray) {

		return update(table, updateColumnArray, appendColumns, null, whereColumnArray, null, null,
				updateParameterArray, whereParameterArray, null);
	}

	/**
	 * 更新操作:update table set... where...
	 * 
	 * @param table
	 *            表名称
	 * @param updateColumnArray
	 *            更新字段(数组形式)
	 * @param updateColumnJoin
	 *            更新字段(文本形式)
	 * @param useTimestamp
	 *            是否更新UPDATE_TIME字段
	 * @param useUpdateId
	 *            是否更新UPDATE_ID字段
	 * @param whereColumnArray
	 *            where条件(数组形式),如new String["field1",
	 *            "field2"]将转成sql中where子句的一部分,field1=? and field2=?
	 * @param whereColumnJoin
	 *            where条件(文本形式),如field1=? and (field2=1 or field3=?). 这种方式比较灵活
	 * @param replaceArray
	 *            将sql语句中出现的若干{IN}依次替换成replaceArray[x]个数的IN(?,?,...)形式
	 * @param updateParameterArray
	 *            与sql语句中?出现次数匹配的对象数组即参数,这里为更新字段对应的参数数组
	 * @param whereParameterArray
	 *            与sql语句中?出现次数匹配的对象数组即参数，这里为where子句中限定字段对应的参数数组
	 * @param cachedParameter
	 *            缓存参数
	 * @return 更新的记录数
	 */
	@Override
	public int update(String table, String[] updateColumnArray, String[] appendColumns,
			String updateColumnJoin, String[] whereColumnArray, String whereColumnJoin,
			int[] replaceArray, Object[] updateParameterArray, Object[] whereParameterArray,
			CachedParameter cachedParameter) {

		if (table == null)
			table = getTable();

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(table).append(" SET ");

		if (appendColumns != null)
			updateColumnArray = ArrayUtil.joinArray(updateColumnArray, appendColumns);

		StringBuilder setSql = setCondition(updateColumnArray, updateColumnJoin);

		sql.append(setSql).append(" WHERE TRUE ");

		StringBuilder whereBuilder = whereCondition(whereColumnArray, whereColumnJoin,
				replaceArray);

		sql.append(whereBuilder);

		Object[] merge = ArrayUtil.arrayMerge(updateParameterArray, whereParameterArray);

		int count = update(getJdbcTemplate(), sql.toString(), merge);

		if (cachedParameter == null) {
			cachedParameter = getCachedParameter();
		}

		// 准备清除缓存
		List<Map<String, Object>> list = null;
		if (cachedParameter != null && cachedParameter.isCacheable()) {
			StringBuilder newSql = new StringBuilder();
			newSql.append("SELECT ID FROM ").append(table).append(" WHERE TRUE ")
					.append(whereBuilder);
			list = queryForList(getJdbcTemplate(), newSql.toString(), whereParameterArray);

		}

		// 开始清除缓存
		if (list != null && list.size() == count) {
			for (Map<String, Object> map : list) {
				cacheHelper.delete((String) map.get("ID"));

			}
		}

		// 清除关联缓存
		if (cachedParameter != null && cachedParameter.getEvict() != null) {
			cacheHelper.delete(cachedParameter.getEvict());
		}

		return count;
	}

	/**
	 * @see #delete(String, String[], String, int[], Object[], CachedParameter)
	 */
	@Override
	public int delete(String table, String[] whereColumnArray, Object[] parameterArray) {

		return delete(table, whereColumnArray, null, null, parameterArray, null);
	}

	/**
	 * 删除操作
	 * 
	 * @param table
	 *            表名称
	 * @param whereColumnArray
	 *            条件字段数组
	 * @param whereColumnJoin
	 *            多个条件字段拼接形式
	 * @param replaceArray
	 *            多条件字段拼接形式中每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * @param parameterArray
	 *            参数值数组
	 * @param cachedParameter
	 *            缓存参数
	 * @return 删除的记录数
	 */
	@Override
	public int delete(String table, String[] whereColumnArray, String whereColumnJoin,
			int[] replaceArray, Object[] parameterArray, CachedParameter cachedParameter) {

		if (table == null)
			table = getTable();

		StringBuilder sql = new StringBuilder();

		sql.append("DELETE FROM ").append(table).append(" WHERE TRUE ");

		StringBuilder whereBuilder = whereCondition(whereColumnArray, whereColumnJoin,
				replaceArray);

		sql.append(whereBuilder);

		if (cachedParameter == null) {
			cachedParameter = getCachedParameter();
		}
		// 准备清除缓存
		List<Map<String, Object>> list = null;
		if (cachedParameter != null && cachedParameter.isCacheable()) {
			StringBuilder newSql = new StringBuilder();
			newSql.append("SELECT ID FROM ").append(table).append(" WHERE TRUE ")
					.append(whereBuilder);
			list = queryForList(getJdbcTemplate(), newSql.toString(), parameterArray);

		}

		int count = update(getJdbcTemplate(), sql.toString(), parameterArray);

		// 开始清除缓存
		if (list != null && list.size() == count) {
			for (Map<String, Object> map : list) {
				cacheHelper.delete((String) map.get("ID"));
			}
		}

		// 清除关联缓存
		if (cachedParameter != null && cachedParameter.getEvict() != null) {
			cacheHelper.delete(cachedParameter.getEvict());
		}

		return count;
	}

	/**
	 * 按照主键id删除一条记录
	 * 
	 * @see JdbcOperationsImpl#delete(String, String[], String, int[], Object[])
	 */
	@Override
	public int deleteById(String table, String id, CachedParameter cachedParameter) {

		if (table == null)
			table = getTable();

		int count = delete(table, new String[] { BaseEntity.idx }, null, null, new Object[] { id },
				new CachedParameter());

		if (cachedParameter == null) {
			cachedParameter = getCachedParameter();
		}
		// 清除缓存
		if (count == 1 && (cachedParameter != null && cachedParameter.isCacheable())) {
			cacheHelper.delete(table + "/" + id);

			// 清除关联缓存
			if (cachedParameter != null && cachedParameter.getEvict() != null) {
				cacheHelper.delete(cachedParameter.getEvict());
			}
		}

		return count;
	}

	/**
	 * @see #deleteById(String, String, CachedParameter)
	 */
	@Override
	public int deleteById(String table, String id) {

		return deleteById(table, id, null);
	}

	/**
	 * 逻辑删除操作，更新表字段IS_DEL='Y'
	 * 
	 * @see #updateById(String, String[], String[], Object[], String)
	 */
	@Override
	public int deleteUpdateById(String table, String[] appendColumns, Object[] updateParameterArray,
			String id) {

		return updateById(table, new String[] { BaseEntity.is_del }, appendColumns,
				updateParameterArray, id);
	}

	/**
	 * 查询单条实例
	 * 
	 * @param clazz
	 *            实例类型
	 * @param table
	 *            表名称
	 * @param selectColumnArray
	 *            查询字段字符数组
	 * @param useId
	 *            是否查询id字段
	 * @param useTimestamp
	 *            是否查询create_time,update_time系统字段
	 * @param useCreator
	 *            是否查询create_id,update_id系统字段
	 * @param whereColumnArray
	 *            条件字段数组
	 * @param whereColumnJoin
	 *            多条件字段拼接形式
	 * @param replaceArray
	 *            多条件字段拼接形式中每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * @param parameterArray
	 *            参数值数组
	 * @return 一条查询数据
	 */
	@Override
	public <T> T get(Class<T> clazz, String table, boolean tableWithWhere,
			String[] selectColumnArray, String[] appendColumns, String[] whereColumnArray,
			String whereColumnJoin, int[] replaceArray, Object[] parameterArray) {

		if (appendColumns != null)
			selectColumnArray = ArrayUtil.joinArray(selectColumnArray, appendColumns);

		if (table == null)
			table = getTable();

		if (selectColumnArray == null || selectColumnArray.length == 0)
			selectColumnArray = new String[] { "*" };

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ").append(ArrayUtil.joinArrayElement(selectColumnArray, ","))
				.append(" FROM ").append(table);
		if (!tableWithWhere)
			sql.append(" WHERE TRUE ");

		sql.append(whereCondition(whereColumnArray, whereColumnJoin, replaceArray));

		return find(getJdbcTemplate(), sql.toString(), clazz, parameterArray);

	}

	/**
	 * 按照主键id查询单条实例
	 * 
	 * @see JdbcOperationsImpl#get(Class, String, String[], String[], String[],
	 *      String, int[], Object[])
	 * 
	 */
	@Override
	public <T> T getById(Class<T> clazz, String table, String[] selectColumnArray,
			String[] appendColumn, String id, CachedParameter cachedParameter) {

		return getByKey(clazz, table, selectColumnArray, appendColumn,
				new String[] { BaseEntity.idx }, new Object[] { id }, cachedParameter);

	}

	/**
	 * 从一组对象值生成缓存key值
	 * 
	 * @param keyValues
	 *            一组对象值
	 * @return 缓存key值
	 */
	private String generateCacheKey(Object[] keyValues) {

		StringBuilder key = new StringBuilder("");
		if (keyValues != null && keyValues.length > 0) {
			for (Object value : keyValues) {
				if (value != null) {
					if (key.length() == 0)
						key.append(String.valueOf(value));
					else
						key.append("-").append(String.valueOf(value));
				}
			}
		}
		return key.toString();
	}

	/**
	 * @see #get(Class, String, boolean, String[], String[], String[], String,
	 *      int[], Object[])
	 */
	@Override
	public <T> T getByKey(Class<T> clazz, String table, String[] selectColumnArray,
			String[] appendColumn, String[] keyColumns, Object[] keyValues,
			CachedParameter cachedParameter) {

		if (table == null)
			table = getTable();

		if (cachedParameter == null) {
			cachedParameter = getCachedParameter();
		}
		// 查找缓存对象
		if (cachedParameter != null && cachedParameter.isCacheable()) {
			T t = cacheHelper.get(table + "/" + generateCacheKey(keyValues));
			if (t != null)
				return t;
		}

		T t = get(clazz, table, false, selectColumnArray, appendColumn, keyColumns, null, null,
				keyValues);

		// 设置缓存对象
		if (t != null && (cachedParameter != null && cachedParameter.isCacheable())) {
			int exp = cachedParameter == null ? CacheHelper.DEFAULT_EXPIRE
					: cachedParameter.getExpire();
			cacheHelper.set(table + "/" + generateCacheKey(keyValues), exp, t);
		}

		return t;
	}

	/**
	 * @see #getById(Class, String, String[], String[], String, CachedParameter)
	 */
	@Override
	public <T> T getById(Class<T> clazz, String table, String[] selectColumnArray,
			String[] appendColumn, String id) {

		return getById(clazz, table, selectColumnArray, appendColumn, id, null);
	}

	@Override
	public <T> List<T> queryPage(PageBean pageBean, Class<T> clazz, String table,
			boolean tableWithWhere, String[] selectColumnArray, String[] appendColumns,
			String[] whereColumnArray, String whereColumnJoin, int[] replaceArray, String orderBy,
			Object[] parameterArray) {

		if (appendColumns != null)
			selectColumnArray = ArrayUtil.joinArray(selectColumnArray, appendColumns);

		if (selectColumnArray == null || selectColumnArray.length == 0)
			selectColumnArray = new String[] { "*" };

		if (table == null)
			table = getTable();

		StringBuilder rowSql = new StringBuilder();
		StringBuilder countSql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder();

		rowSql.append("SELECT ").append(ArrayUtil.joinArrayElement(selectColumnArray, ","))
				.append(" FROM ").append(table);

		countSql.append("SELECT COUNT(*) FROM ").append(table);

		if (!tableWithWhere) {
			rowSql.append(" WHERE TRUE ");
			countSql.append(" WHERE TRUE ");
		}

		whereSql.append(whereCondition(whereColumnArray, whereColumnJoin, replaceArray));

		if (pageBean != null) {
			if (!pageBean.getParameters().isEmpty()) {
				if (parameterArray != null && parameterArray.length > 0)
					pageBean.getParameters().addAll(0, Arrays.asList(parameterArray));
				parameterArray = pageBean.getParameters().toArray();
			}

			if (!CommonUtil.isEmpty(pageBean.getSqlWhere()))
				whereSql.append(" AND ( ").append(pageBean.getSqlWhere()).append(" ) ");

			if (!CommonUtil.isEmpty(pageBean.getSqlOrder()))
				orderBy = pageBean.getSqlOrder();
		}

		rowSql.append(whereSql);

		if (!CommonUtil.isEmpty(orderBy))
			rowSql.append(" ORDER BY ").append(orderBy).append(" ");

		if (pageBean != null && pageBean.isPageShow()) {
			countSql.append(whereSql);
			return queryPage(clazz, pageBean, countSql, rowSql, parameterArray);
		} else {

			List<T> resultList = query(getJdbcTemplate(), rowSql.toString(), clazz, parameterArray);
			return resultList;
		}
	}

	@Override
	public <T> List<T> queryPage(PageBean pageBean, Class<T> clazz, String table,
			boolean tableWithWhere, String[] selectColumnArray, String[] appendColumns,
			String[] whereColumnArray, String whereColumnJoin, int[] replaceArray, String orderBy,
			String groupBy, String[] havingColumnArray, String orderByAfterGroupBy,
			Object[] parameterArray) {

		if (appendColumns != null)
			selectColumnArray = ArrayUtil.joinArray(selectColumnArray, appendColumns);

		if (selectColumnArray == null || selectColumnArray.length == 0)
			selectColumnArray = new String[] { "*" };

		if (table == null)
			table = getTable();

		StringBuilder rowSql = new StringBuilder();
		StringBuilder countSql = new StringBuilder();
		StringBuilder whereSql = new StringBuilder();
		StringBuilder havingSql = new StringBuilder();

		rowSql.append("SELECT ").append(ArrayUtil.joinArrayElement(selectColumnArray, ","))
				.append(" FROM ").append(table);
		if (CommonUtil.isEmpty(groupBy))
			countSql.append("SELECT COUNT(*) FROM ").append(table);
		else
			countSql.append("SELECT COUNT(*) FROM (SELECT " + groupBy + " FROM ").append(table);

		if (!tableWithWhere) {
			rowSql.append(" WHERE TRUE ");
			countSql.append(" WHERE TRUE ");
		}

		whereSql.append(whereCondition(whereColumnArray, whereColumnJoin, replaceArray));

		if (pageBean != null) {
			if (!pageBean.getParameters().isEmpty()) {
				if (parameterArray != null && parameterArray.length > 0)
					pageBean.getParameters().addAll(0, Arrays.asList(parameterArray));
				parameterArray = pageBean.getParameters().toArray();
			}

			if (!CommonUtil.isEmpty(pageBean.getSqlWhere()))
				whereSql.append(" AND ( ").append(pageBean.getSqlWhere()).append(" ) ");

			if (!CommonUtil.isEmpty(pageBean.getSqlOrder()))
				orderBy = pageBean.getSqlOrder();
		}

		rowSql.append(whereSql);
		countSql.append(whereSql);

		if (!CommonUtil.isEmpty(orderBy) && CommonUtil.isEmpty(groupBy))
			rowSql.append(" ORDER BY ").append(orderBy).append(" ");

		// 分组处理
		if (!CommonUtil.isEmpty(groupBy)) {
			havingSql.append(" GROUP BY ").append(groupBy);
			if (havingColumnArray != null && havingColumnArray.length > 0)
				havingSql.append(" HAVING ")
						.append(ArrayUtil.joinArrayElement(havingColumnArray, " AND ", null, "=?"));

			rowSql.append(havingSql);
			countSql.append(havingSql).append(" ) AS temp_table ");
			if (!CommonUtil.isEmpty(orderByAfterGroupBy))
				rowSql.append(" ORDER BY ").append(orderByAfterGroupBy);
		}

		if (pageBean != null && pageBean.isPageShow()) {
			return queryPage(clazz, pageBean, countSql, rowSql, parameterArray);
		} else {
			List<T> resultList = query(getJdbcTemplate(), rowSql.toString(), clazz, parameterArray);
			return resultList;
		}
	}

	/**
	 * @see #queryPage(PageBean, Class, String, boolean, String[], String[],
	 *      String[], String, int[], String, Object[])
	 */
	@Override
	public <T> List<T> query(PageBean page, Class<T> clazz, String table,
			String[] selectColumnArray, String[] appendColumns, String[] whereColumnArray,
			Object[] parameterArray) {

		return queryPage(page, clazz, table, false, selectColumnArray, appendColumns,
				whereColumnArray, null, null, null, parameterArray);
	}

	/**
	 * 分页操作，先查询总记录数，再查询页数据
	 * 
	 * @param clazz
	 *            实体类
	 * @param pageBean
	 *            分页信息封装类
	 * @param countSql
	 *            记录总数计数语句
	 * @param rowSql
	 *            分页查询语句
	 * @param parameterArray
	 *            参数值数组
	 * @return 一组查询记录
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> queryPage(Class<T> clazz, PageBean pageBean, StringBuilder countSql,
			StringBuilder rowSql, Object[] parameterArray) {

		long count = queryForLong(getJdbcTemplate(), countSql.toString(), parameterArray);
		pageBean.setTotalCount((int) count);

		rowSql.append(" LIMIT ").append(pageBean.getPageSize()).append(" OFFSET ")
				.append(pageBean.getStartIndex());

		if (clazz == null) {
			List<Map<String, Object>> itemList = queryForList(getJdbcTemplate(), rowSql.toString(),
					parameterArray);
			pageBean.setItemList(itemList);
			return (List<T>) itemList;
		} else {
			List<T> voList = query(getJdbcTemplate(), rowSql.toString(), clazz, parameterArray);
			pageBean.setVoList(voList);
			return voList;
		}
	}

	/**
	 * 查找树型结构当前父节点下子节点id的最大值，生成下一个子结点id的值
	 * 
	 * @param parentId
	 *            父节点的值
	 * @param table
	 *            表名称
	 * @param parentIdColumn
	 *            父节点字段名
	 * @param idColumn
	 *            子节点字段名
	 * @return 下一个子结点主键id的值
	 */
	@Override
	public synchronized String getNextId(String parentId, String table, String parentIdColumn,
			String idColumn) {

		if (table == null)
			table = getTable();

		String sql = null;
		if (CommonUtil.isEmpty(parentId)) {
			sql = "SELECT MAX(" + idColumn + ") AS MAXID FROM " + table + " WHERE " + parentIdColumn
					+ " IS NULL ";
		} else {
			sql = "SELECT MAX(" + idColumn + ") AS MAXID FROM " + table + " WHERE " + parentIdColumn
					+ " = '" + parentId + "'";
		}
		String maxId = null;
		Map<String, Object> map = queryForMap(getJdbcTemplate(), sql);

		if (map != null) {
			maxId = (String) map.get("MAXID");
		}

		if (maxId == null) {
			return parentId == null ? "001" : parentId + "001";
		}

		String pid = maxId.substring(0, maxId.length() - 3);
		String id = maxId.substring(maxId.length() - 3);
		int number = 0;
		try {
			number = Integer.parseInt(id);
		} catch (Exception e) {
			;
		}

		if (number == 999)
			throw new RuntimeException("the id had exceed the max value.");

		String nextId = String.valueOf(number + 1);
		return pid + CommonUtil.fillPrefixToLength(nextId, "0", 3);
	}

	/**
	 * 插入或更新时唯一性约束的验证
	 * 
	 * @param table
	 *            表名称
	 * @param countField
	 *            count字段名
	 * @param uniqueFieldNames
	 *            联合唯一字段名数组
	 * @param uniqueFieldValues
	 *            联合唯一字段值数组
	 * @param pkFieldNames
	 *            联合主键名数组，只在更新时用到
	 * @param pkFieldValues
	 *            联合主键值数组，只在更新时用到
	 * @return 记录是否唯一
	 */
	@Override
	public boolean checkUnique(String table, String countField, String[] uniqueFieldNames,
			Object[] uniqueFieldValues, String[] pkFieldNames, Object[] pkFieldValues) {

		if (table == null)
			table = getTable();

		StringBuilder sql = new StringBuilder("SELECT ");
		if (countField == null)
			sql.append("COUNT(*) ");
		else
			sql.append("COUNT(").append(countField).append(") ");
		sql.append("FROM ");

		sql.append(table).append(" WHERE ");
		sql.append(ArrayUtil.joinArrayElement(uniqueFieldNames, " AND ", null, "=?"));

		List<Object> list = new ArrayList<Object>();
		list.addAll(Arrays.asList(uniqueFieldValues));

		if (pkFieldNames != null && pkFieldNames.length > 0) {
			sql.append(" AND (");
			sql.append(ArrayUtil.joinArrayElement(pkFieldNames, " OR ", null, "<>?"));
			sql.append(")");
			list.addAll(Arrays.asList(pkFieldValues));
		}

		int count = queryForInt(getJdbcTemplate(), sql.toString(), list.toArray());
		if (count == 0)
			return true;
		else
			return false;
	}

	/**
	 * @see #checkUnique(String, String, String[], Object[], String[], Object[])
	 */
	@Override
	public boolean checkUnique(String[] uniqueFieldNames, Object[] uniqueFieldValues,
			String[] pkFieldNames, Object[] pkFieldValues) {

		return checkUnique(null, null, uniqueFieldNames, uniqueFieldValues, pkFieldNames,
				pkFieldValues);
	}

	/**
	 * 插入一条记录
	 * 
	 * @param vo
	 *            待插入数据
	 * @return 返回记录主键
	 */
	@Override
	public String add(Map<String, Object> vo) {

		throw new UnsupportedOperationException();
	}

	/**
	 * 插入一条记录
	 * 
	 * @param vo
	 *            待插入数据
	 * @return 返回记录主键
	 */
	@Override
	public String insert(Map<String, ? extends Object> vo) {

		throw new UnsupportedOperationException();
	}

	/**
	 * 更新一条记录
	 * 
	 * @param vo
	 *            待更新数据
	 * @return 返回更新记录数
	 */
	@Override
	public int update(Map<String, Object> vo) {

		throw new UnsupportedOperationException();
	}

	/**
	 * 删除一条记录
	 * 
	 * @param id
	 *            记录主键
	 * @return 删除记录数
	 */
	@Override
	public int delete(String id) {

		return deleteById(null, id, null);

	}

	/**
	 * 查询一条记录
	 * 
	 * @param id
	 *            记录主键
	 * @return 查询结果
	 */
	@Override
	public Map<String, Object> get(String id) {

		return getById(null, null, null, null, id);
	}

	/**
	 * 插入一条记录
	 * 
	 * @param vo
	 *            待插入数据
	 * @return 返回记录主键
	 */
	@Override
	public String add(E vo) {

		throw new UnsupportedOperationException();
	}

	/**
	 * 插入一条记录
	 * 
	 * @param vo
	 *            待更新数据
	 * @return 返回更新记录数
	 */
	@Override
	public int update(E vo) {

		throw new UnsupportedOperationException();
	}

	/**
	 * 查询一条记录
	 * 
	 * @param id
	 *            记录主键
	 * @param clazz
	 *            实体类
	 * @return 查询结果
	 */
	@Override
	public E get(String id, Class<E> clazz) {

		return getById(clazz, null, null, null, id);
	}

	/**
	 * @see #query(PageBean, Class, String, String[], String[], String[],
	 *      Object[])
	 */
	@Override
	public List<E> query(PageBean page, Class<E> clazz) {

		return query(page, clazz, null, null, null, null, null);
	}

	/**
	 * @see #query(PageBean, Class, String, String[], String[], String[],
	 *      Object[])
	 */
	@Override
	public List<Map<String, Object>> query(PageBean page) {

		return query(page, null, null, null, null, null, null);
	}

	/**
	 * 排除被标记删除的记录,即只返回is_del=N的记录
	 * 
	 * @param page
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryExcludeDeleted(PageBean page) {

		return query(page, null, null, null, null, new String[] { BaseEntity.is_del },
				new Object[] { ConstantUtil.FLAG_NO });
	}

	@Override
	public int deleteMark(String id, String updateId) {

		return updateById(table, new String[] { BaseEntity.is_del }, NAMES_UT_UID,
				new Object[] { ConstantUtil.FLAG_YES, DateTimeUtil.currentDateTime(), updateId },
				id);
	}

	@Override
	public Map<String, Object> getByProperties(String[] keyColumns, Object[] keyValues) {

		return getByKey(null, getTable(), null, null, keyColumns, keyValues, null);
	}

	public Map<String, Object> findUniqueByProperty(String property, Object value) {

		return getByKey(null, null, null, null, new String[] { property }, new Object[] { value },
				null);
	}

	private class DAOOperator {
		private String operator;
		private Timestamp operTime;

		public DAOOperator(String operator) {
			this.operator = operator;
			this.operTime = DateTimeUtil.currentDateTime();
		}

		public String getOperator() {

			return operator;
		}

		public Timestamp getOperTime() {

			return operTime;
		}
	}

	private DAOOperator fixOperator(Map<String, Object> entity) {

		String operator = (String) entity.get(BaseEntity.create_id) != null
				? (String) entity.get(BaseEntity.create_id)
				: (String) entity.get(BaseEntity.update_id);
		entity.remove(BaseEntity.create_id);
		entity.remove(BaseEntity.update_id);

		if (operator == null) {
			throw new DAOException("没有创建人或者更新人!");
		}

		DAOOperator oper = new DAOOperator(operator);
		return oper;
	}

	private void fixFields(List<String> fields, List<Object> values, Map<String, Object> entity) {

		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Class<?> entityClass = (Class<?>) params[0];

		for (String key : entity.keySet()) {
			if (key.equals(BaseEntity.create_id) || key.equals(BaseEntity.update_id)
					|| key.equals(BaseEntity.update_time) || key.equals(BaseEntity.create_time)) {
				continue;
			}

			try {
				Field f = entityClass.getDeclaredField(key);
				if (f != null) {
					fields.add(key);
					values.add(entity.get(key));
				}
			} catch (NoSuchFieldException e) {

			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String create(Map<String, Object> entity) {

		DAOOperator oper = fixOperator(entity);

		List<String> fields = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();

		fixFields(fields, values, entity);

		String id = CommonUtil.getUUID();

		List<Object> others = new ArrayList<Object>();
		others.add(id);
		others.add(oper.getOperTime());
		others.add(oper.getOperTime());
		others.add(oper.getOperator());
		others.add(oper.getOperator());

		values.addAll(others);

		add(getTable(), fields.toArray(new String[] {}), NAMES, values.toArray());

		return id;
	}

}
