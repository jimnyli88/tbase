package cn.com.ut.core.dal.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.ut.core.cache.CachedParameter;
import cn.com.ut.core.common.jdbc.PageBean;

public interface JdbcOperation<E extends BaseEntity> extends SpringJdbcOperationsApi {

	String getTable(Class<? extends BaseEntity> entiry);

	String getTable();

	public JdbcTemplate getJdbcTemplate();

	long count(String table, String countColumn, String[] whereColumnArray, String whereColumnJoin,
			int[] replaceArray, Object[] parameterArray);

	long count(String table, String countColumn, String[] whereColumnArray,
			Object[] parameterArray);

	@Deprecated
	int addOne(String table, String[] insertColumnArray, boolean useId, boolean useTimestamp,
			boolean useCreator, Object[] parameterArray, DAOParameter... daoParameters);

	int add(String table, String[] insertColumnArray, String[] appendColumns,
			Object[] parameterArray, CachedParameter cachedParameter);

	int add(String table, String[] insertColumnArray, String[] appendColumns,
			Object[] parameterArray);

	public int[] addBatch(String table, String[] insertColumnArray, String[] appendColumns,
			List<Object[]> parameterArrayBatch);

	int updateById(String table, String[] updateColumnArray, String[] appendColumns,
			String updateColumnJoin, Object[] updateParameterArray, String id,
			CachedParameter cachedParameter);

	int updateById(String table, String[] updateColumnArray, String[] appendColumns,
			Object[] updateParameterArray, String id);

	int update(String table, String[] updateColumnArray, String[] appendColumns,
			String updateColumnJoin, String[] whereColumnArray, String whereColumnJoin,
			int[] replaceArray, Object[] updateParameterArray, Object[] whereParameterArray,
			CachedParameter cachedParameter);

	int update(String table, String[] updateColumnArray, String[] appendColumns,
			String[] whereColumnArray, Object[] updateParameterArray, Object[] whereParameterArray);

	int delete(String table, String[] whereColumnArray, String whereColumnJoin, int[] replaceArray,
			Object[] parameterArray, CachedParameter cachedParameter);

	public int delete(String table, String[] whereColumnArray, Object[] parameterArray);

	int deleteById(String table, String id, CachedParameter cachedParameter);

	int deleteById(String table, String id);

	int deleteUpdateById(String table, String[] appendColumns, Object[] updateParameterArray,
			String id);

	<T> T get(Class<T> clazz, String table, boolean tableWithWhere, String[] selectColumnArray,
			String[] appendColumns, String[] whereColumnArray, String whereColumnJoin,
			int[] replaceArray, Object[] parameterArray);

	<T> T getById(Class<T> clazz, String table, String[] selectColumnArray, String[] appendColumn,
			String id, CachedParameter cachedParameter);

	<T> T getById(Class<T> clazz, String table, String[] selectColumnArray, String[] appendColumn,
			String id);

	public <T> T getByKey(Class<T> clazz, String table, String[] selectColumnArray,
			String[] appendColumn, String[] keyColumns, Object[] keyValues,
			CachedParameter cachedParameter);

	public String getNextId(String parentId, String table, String parentIdColumn, String idColumn);

	@Deprecated
	<T> List<T> queryAll(Class<T> clazz, String table, String[] selectColumnArray, boolean useId,
			boolean useTimestamp, boolean useCreator, String[] whereColumnArray,
			String whereColumnJoin, int[] replaceArray, String orderBy, Object[] parameterArray,
			DAOParameter... daoParameters);

	@Deprecated
	List<Map<String, Object>> queryForList(final String sql, final Object[] parameterArray,
			String orderBy, String[] shardNames);

	/**
	 * sql子句中的每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * 
	 * @param sql
	 *            语句
	 * @param replaceArray
	 *            占位符数量数组
	 * @return 替换后的语句
	 */
	String replaceInCase(String sql, int[] replaceArray);

	/**
	 * 查询操作，可以分页，可接收动态查询条件
	 * 
	 * @param pageBean
	 * @param clazz
	 *            实体类
	 * @param table
	 *            表名称
	 * @param tableWithWhere
	 * @param selectColumnArray
	 *            查询返回字段数组
	 * @param appendColumns
	 * @param whereColumnArray
	 *            条件字段数组
	 * @param whereColumnJoin
	 *            多条件字段拼接形式
	 * @param replaceArray
	 *            多条件字段拼接形式中每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * @param orderBy
	 *            排序字段
	 * @param parameterArray
	 *            参数值数组
	 * @return
	 */
	<T> List<T> queryPage(PageBean pageBean, Class<T> clazz, String table, boolean tableWithWhere,
			String[] selectColumnArray, String[] appendColumns, String[] whereColumnArray,
			String whereColumnJoin, int[] replaceArray, String orderBy, Object[] parameterArray);

	/**
	 * 查询操作，可以分页，可接收动态查询条件
	 * 
	 * @param pageBean
	 * @param clazz
	 *            实体类
	 * @param table
	 *            表名称
	 * @param tableWithWhere
	 * @param selectColumnArray
	 *            查询返回字段数组
	 * @param appendColumns
	 * @param whereColumnArray
	 *            条件字段数组
	 * @param whereColumnJoin
	 *            多条件字段拼接形式
	 * @param replaceArray
	 *            多条件字段拼接形式中每个{IN}按出现顺序需要替换为指定个数的?占位符
	 * @param orderBy
	 *            排序字段
	 * @param groupBy
	 *            分组
	 * @param havingColumnArray
	 *            分组后条件字段数组
	 * @param orderByAfterGroupBy
	 *            分组完毕后排序
	 * @param parameterArray
	 *            参数值数组
	 * @return
	 */
	<T> List<T> queryPage(PageBean pageBean, Class<T> clazz, String table, boolean tableWithWhere,
			String[] selectColumnArray, String[] appendColumns, String[] whereColumnArray,
			String whereColumnJoin, int[] replaceArray, String orderBy, String groupBy,
			String[] havingColumnArray, String orderByAfterGroupBy, Object[] parameterArray);

	<T> List<T> query(PageBean page, Class<T> clazz, String table, String[] selectColumnArray,
			String[] appendColumns, String[] whereColumnArray, Object[] parameterArray);

	boolean checkUnique(String table, String countField, String[] uniqueFieldNames,
			Object[] uniqueFieldValues, String[] pkFieldNames, Object[] pkFieldValues);

	boolean checkUnique(String[] uniqueFieldNames, Object[] uniqueFieldValues,
			String[] pkFieldNames, Object[] pkFieldValues);

	String add(Map<String, Object> vo);

	String add(String[] insertColumnArray, Map<String, Object> vo);

	String insert(Map<String, ? extends Object> vo);

	int update(Map<String, Object> vo);

	int update(String[] updateColumnArray, Map<String, Object> vo);

	Map<String, Object> get(String id);

	/**
	 * 插入一条记录
	 * 
	 * @param vo
	 */
	String add(E vo);

	/**
	 * 更新一条记录
	 * 
	 * @param vo
	 */
	int update(E vo);

	/**
	 * 获取一条记录
	 * 
	 * @param id
	 * @return
	 */
	E get(String id, Class<E> clazz);

	/**
	 * 删除一条记录
	 * 
	 * @param id
	 */
	int delete(String id);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<E> query(PageBean page, Class<E> clazz);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> query(PageBean page);

	/**
	 * 标记删除，将指定记录的is_del字段值改为Y
	 * 
	 * @param id
	 * @return 影响的记录数
	 */
	int deleteMark(String id, String updateId);

	/**
	 * 排除被标记删除的记录,即只返回is_del=N的记录(只限单表)
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryExcludeDeleted(PageBean page);

	int[] addVoBatch(String table, String[] insertColumnArray, String[] appendColumns,
			List<Map<String, Object>> vos);

	public Map<String, Object> getByProperties(String[] keyColumns, Object[] keyValues);

	public Map<String, Object> findUniqueByProperty(String property, Object value);

	public String create(Map<String, Object> entity);

	void init();

	<T extends Object> int deleteBatch(Collection<T> fieldValues, String fieldName);

	<T extends Object> int deleteBatch(Collection<T> fieldValues);

	int updateById(String[] updateColumnArray, Object[] updateParameterArray, String id);

	int update(String[] updateColumnArray, String[] whereColumnArray, Object[] updateParameterArray,
			Object[] whereParameterArray);

	List<Map<String, Object>> queryPage(PageBean pageBean, String[] selectColumnArray,
			String[] whereColumnArray, Object[] parameterArray);

	<T> List<T> queryPage(PageBean pageBean, Class<T> clazz, String[] selectColumnArray,
			String[] whereColumnArray, Object[] parameterArray);

	<T> T getById(Class<T> clazz, String[] selectColumnArray, String id);

	Map<String, Object> getById(String[] selectColumnArray, String id);

	<T> T getByKey(Class<T> clazz, String[] selectColumnArray, String[] keyColumns,
			Object[] keyValues);

	Map<String, Object> getByKey(String[] selectColumnArray, String[] keyColumns,
			Object[] keyValues);

	List<Map<String, Object>> query(PageBean page, String[] selectColumnArray,
			String[] whereColumnArray, Object[] parameterArray);

}