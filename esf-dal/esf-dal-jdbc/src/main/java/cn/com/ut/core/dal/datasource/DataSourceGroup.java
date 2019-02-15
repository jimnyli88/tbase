package cn.com.ut.core.dal.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 逻辑数据源管理
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
public class DataSourceGroup {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceGroup.class);

	/**
	 * 逻辑数据源下的一组数据源分片
	 */
	private Map<String, DataSourceShard> shards = new LinkedHashMap<String, DataSourceShard>();

	/**
	 * 逻辑数据源名称唯一标识
	 */
	private String groupName;

	public Map<String, DataSourceShard> getShards() {

		return shards;
	}

	public void setShards(Map<String, DataSourceShard> shards) {

		this.shards = shards;
	}

	public String getGroupName() {

		return groupName;
	}

	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	/**
	 * 无分片时默认获取第一个DataSourceShard
	 */
	public DataSourceShard getMainDataSourceShard() {

		if (shards.isEmpty())
			return null;
		return shards.values().iterator().next();
	}

	/**
	 * 根据ShardName获取DataSourceShard
	 */
	public DataSourceShard getDataSourceShardByName(String shardName) {

		return shards.get(shardName);
	}
}
