package cn.com.ut.core.dal.jdbc;
/**
* 分片参数
* @author wuxiaohua
* @since 2013-12-22下午2:27:50
*/
public class ShardParameter implements DAOParameter {

	private String[] shardNames;

	public String[] getShardNames() {

		return shardNames;
	}

	public void setShardName(String[] shardNames) {

		this.shardNames = shardNames;
	}

	public ShardParameter() {

	}

	public ShardParameter(String[] shardName) {

		setShardName(shardName);
	}
}
