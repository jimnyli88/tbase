package cn.com.ut.core.dal.jdbc;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 实体类共同基类
 * 
 * @author wuxiaohua
 * @since 2014-2-27
 */
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id;
	protected String createId;
	protected String updateId;
	protected Timestamp createTime;
	protected Timestamp updateTime;
	protected String remark;

	public static final String idx = "id";
	public static final String create_id = "create_id";
	public static final String create_time = "create_time";
	public static final String update_id = "update_id";
	public static final String update_time = "update_time";
	public static final String is_del = "is_del";
	public static final String is_use = "is_use";
	public static final String user_id = "user_id";

	public static final String[] _create = new String[] { idx, create_id, create_time };
	public static final String[] _update = new String[] { update_id, update_time };

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getCreateId() {

		return createId;
	}

	public void setCreateId(String createId) {

		this.createId = createId;
	}

	public String getUpdateId() {

		return updateId;
	}

	public void setUpdateId(String updateId) {

		this.updateId = updateId;
	}

	public Timestamp getCreateTime() {

		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {

		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {

		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {

		this.updateTime = updateTime;
	}

	public String getRemark() {

		return remark;
	}

	public void setRemark(String remark) {

		this.remark = remark;
	}

}
