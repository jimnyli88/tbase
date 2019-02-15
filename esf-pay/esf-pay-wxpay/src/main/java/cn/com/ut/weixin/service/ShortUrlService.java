package cn.com.ut.weixin.service;

import cn.com.ut.weixin.config.BaseConfigure;

/**
 * 长码转短码
 * 
 * @author ouyuexing
 *
 */
public class ShortUrlService extends BaseService {

	public ShortUrlService(String certLocalPath, String certPassword)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		super(BaseConfigure.SHORTURL_API, false, certLocalPath, certPassword);
	}

}
