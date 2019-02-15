package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 查询退款
 * 
 * @author ouyuexing
 *
 */
public class RefundQueryService extends BaseService {

	public RefundQueryService(String certLocalPath, String certPassword)
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		super(BaseConfigure.REFUND_QUERY_API, false, certLocalPath, certPassword);
	}

}
