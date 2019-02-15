package cn.com.ut.core.restful;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wuxiaohua
 * @since 2018/7/2
 */

@Getter
@Setter
public class RestInfo implements Serializable {

	private static final long serialVersionUID = 530962171506984534L;

	@JSONField(serialize = false)
	@JsonIgnore
	protected String userId;
	
}
