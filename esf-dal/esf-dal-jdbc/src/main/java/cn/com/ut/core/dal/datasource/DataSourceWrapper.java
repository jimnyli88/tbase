package cn.com.ut.core.dal.datasource;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据源包装器
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
public class DataSourceWrapper {

	/**
	 * 所属组
	 */
	private String groupName;
	/**
	 * 数据源名称
	 */
	private String dataSourceName;
	/**
	 * 逻辑数据库
	 */
	private String logicSchema;
	/**
	 * 物理数据库
	 */
	private String physicalSchema;
	/**
	 * 是否写库
	 */
	private boolean writeable = false;
	/**
	 * 真实数据源
	 */
	private DataSource dataSource;
	/**
	 * jdbc操作模板
	 */
	private JdbcTemplate jdbcTemplate;
	/**
	 * 最近访问时间
	 */
	private long lastAccess = System.nanoTime();
	/**
	 * 是否可用
	 */
	private boolean available = true;
	/**
	 * 权重
	 */
	private int weight = 1;
	/**
	 * 访问计数
	 */
	protected AtomicInteger counter = new AtomicInteger(1);

	public String getGroupName() {

		return groupName;
	}

	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	public String getLogicSchema() {

		return logicSchema;
	}

	public void setLogicSchema(String logicSchema) {

		this.logicSchema = logicSchema;
	}

	public String getPhysicalSchema() {

		return physicalSchema;
	}

	public void setPhysicalSchema(String physicalSchema) {

		this.physicalSchema = physicalSchema;
	}

	public boolean isWriteable() {

		return writeable;
	}

	public void setWriteable(boolean writeable) {

		this.writeable = writeable;
	}

	public DataSource getDataSource() {

		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {

		this.dataSource = dataSource;
	}

	public long getLastAccess() {

		return lastAccess;
	}

	public void setLastAccess(long lastAccess) {

		this.lastAccess = lastAccess;
	}

	public String getDataSourceName() {

		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {

		this.dataSourceName = dataSourceName;
	}

	public JdbcTemplate getJdbcTemplate() {

		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {

		this.jdbcTemplate = jdbcTemplate;
	}

	public boolean isAvailable() {

		return available;
	}

	public void setAvailable(boolean available) {

		this.available = available;
	}

	public int getWeight() {

		return weight;
	}

	public void setWeight(int weight) {

		this.weight = weight;
	}

}
