package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 关闭微信支付订单
 * 
 * @author ouyuexing
 *
 */
public class CloseOrderService extends BaseService {

	public CloseOrderService(String certLocalPath, String certPassword)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		super(BaseConfigure.CLOSE_ORDER, false, certLocalPath, certPassword);
	}

}
