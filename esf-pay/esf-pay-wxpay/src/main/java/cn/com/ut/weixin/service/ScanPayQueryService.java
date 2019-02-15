package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 查询订单
 * 
 * @author ouyuexing
 *
 */
public class ScanPayQueryService extends BaseService {

	public ScanPayQueryService(String certLocalPath, String certPassword)
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		super(BaseConfigure.PAY_QUERY_API, false, certLocalPath, certPassword);
	}

}
