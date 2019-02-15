package cn.com.ut.core.dal.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源分片管理
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
public class DataSourceShard {

	/**
	 * 分片名称
	 */
	private String shardName;

	/**
	 * master
	 */
	private DataSourceWrapper master;

	/**
	 * 一组slave
	 */
	private List<DataSourceWrapper> slaves = new ArrayList<DataSourceWrapper>();

	private int index = -1;

	public int getIndex() {

		return index;
	}

	public void setIndex(int index) {

		this.index = index;
	}

	public String getShardName() {

		return shardName;
	}

	public void setShardName(String shardName) {

		this.shardName = shardName;
	}

	public DataSourceWrapper getMaster() {

		return master;
	}

	public void setMaster(DataSourceWrapper master) {

		this.master = master;
	}

	public List<DataSourceWrapper> getSlaves() {

		return slaves;
	}

	public void setSlaves(List<DataSourceWrapper> slaves) {

		this.slaves = slaves;
	}

}
