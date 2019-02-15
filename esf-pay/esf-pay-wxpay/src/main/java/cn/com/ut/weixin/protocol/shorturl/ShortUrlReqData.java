package cn.com.ut.weixin.protocol.shorturl;

import cn.com.ut.weixin.common.WeiXinField;
import cn.com.ut.weixin.protocol.ModuleBase;

/**
 * 长码转短码
 * 
 * @author ouyuexing
 *
 */
public class ShortUrlReqData extends ModuleBase {

	/**
	 * URL链接
	 */
	@WeiXinField("long_url")
	private String longUrl;

	public String getLongUrl() {

		return longUrl;
	}

	public void setLongUrl(String longUrl) {

		this.longUrl = longUrl;
	}

}
