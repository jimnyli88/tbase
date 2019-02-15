package cn.com.ut.core.common.jdbc;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页实体
 * 
 * @author wuxiaohua
 * @since 2018/7/3
 */
@Getter
@Setter
public class PageInfo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1466435650850691126L;

	/**
	 * 单页显示的记录数
	 */
	private int pagesize = 10;

	/**
	 * 当前页
	 */
	private int pageno = 0;
	/**
	 * 总记录数
	 */
	private long records = 0;
	/**
	 * 总页数
	 */
	private long total = 0;

	/**
	 * 记录
	 */
	@JSONField(serialize = false)
	@JsonIgnore
	private transient List<?> voList;

	public <T> List<T> getVoList() {

		return (List<T>) this.voList;
	}

	public static PageInfo build() {

		return new PageInfo();
	}

	public <T> PageInfo appendPage(Page<T> page) {

		this.setPageno(page.getNumber() + 1);
		this.setPagesize(page.getSize());
		this.setRecords(page.getTotalElements());
		this.setTotal(page.getTotalPages());
		return this;
	}

	public void setRecords(long records) {

		this.records = records;
		total = (this.records + pagesize - 1) / pagesize;
	}

}
