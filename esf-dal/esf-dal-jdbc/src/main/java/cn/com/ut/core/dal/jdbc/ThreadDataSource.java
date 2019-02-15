package cn.com.ut.core.dal.jdbc;

/**
 * DAO读或写方法需连接的数据源标识
 * 
 * @author wuxiaohua
 * @since 2014-2-27
 */
public class ThreadDataSource {

	private String groupName;
	private boolean writeable = false;
	private String shardName;

	public ThreadDataSource() {

	}

	public String getShardName() {

		return shardName;
	}

	public void setShardName(String shardName) {

		this.shardName = shardName;
	}

	public ThreadDataSource(String groupName, boolean writeable) {

		this.groupName = groupName;
		this.writeable = writeable;
	}

	public String getGroupName() {

		return groupName;
	}

	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	public boolean isWriteable() {

		return writeable;
	}

	public void setWriteable(boolean writeable) {

		this.writeable = writeable;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThreadDataSource other = (ThreadDataSource) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "ThreadDataSource [logicSchema=" + groupName + ", writeable=" + writeable + "]";
	}

}
