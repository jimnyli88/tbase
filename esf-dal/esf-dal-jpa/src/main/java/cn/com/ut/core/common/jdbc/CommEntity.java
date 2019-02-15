package cn.com.ut.core.common.jdbc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuxiaohua
 * @since 2018/6/28
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public class CommEntity {

	@Id
	@GenericGenerator(name = "idGenerator", strategy = "cn.com.ut.core.common.jdbc.SequenceGenerator")
	@GeneratedValue(generator = "idGenerator")
	protected String id;
	@Column(name = "is_del", insertable = false)
	protected String isDel = "N";
	@Column(name = "create_time", updatable = false)
	protected Date createTime;
	@Column(name = "create_id", updatable = false)
	protected String createId;
	@Column(name = "update_time", insertable = true)
	protected Date updateTime;
	@Column(name = "update_id", insertable = false)
	protected String updateId;

}
