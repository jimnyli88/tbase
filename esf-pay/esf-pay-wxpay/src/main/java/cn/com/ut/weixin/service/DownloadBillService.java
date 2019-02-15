package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 
 * @author ouyuexing
 * @since 2017年6月28日
 */
public class DownloadBillService extends BaseService {

	public DownloadBillService(String certLocalPath, String certPassword)
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		super(BaseConfigure.DOWNLOAD_BILL_API, false, certLocalPath, certPassword);
	}

	// ALL，返回当日所有订单信息，默认值
	public static final String BILL_TYPE_ALL = "ALL";

	// SUCCESS，返回当日成功支付的订单
	public static final String BILL_TYPE_SUCCESS = "SUCCESS";

	// REFUND，返回当日退款订单
	public static final String BILL_TYPE_REFUND = "REFUND";

	// REVOKED，已撤销的订单
	public static final String BILL_TYPE_REVOKE = "REVOKE";

}
