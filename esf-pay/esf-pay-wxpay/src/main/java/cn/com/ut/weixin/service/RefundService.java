package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 申请退款
 * 
 * @author ouyuexing
 *
 */
public class RefundService extends BaseService {

	public RefundService(String certLocalPath, String certPassword)
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		super(BaseConfigure.REFUND_API, true, certLocalPath, certPassword);
	}

}
