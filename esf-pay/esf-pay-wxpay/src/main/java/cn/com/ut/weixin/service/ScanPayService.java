package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 统一下单
 * 
 * @author ouyuexing
 *
 */
public class ScanPayService extends BaseService {

	public ScanPayService(String certLocalPath, String certPassword)
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		super(BaseConfigure.UNIFIEDORDER_API, false, certLocalPath, certPassword);
	}
}
