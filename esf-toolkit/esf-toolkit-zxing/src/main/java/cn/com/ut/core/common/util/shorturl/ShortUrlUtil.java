package cn.com.ut.core.common.util.shorturl;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import cn.com.ut.core.common.util.CommonUtil;
/**
* 获取短连接工具类
* @author wuxiaohua
* @since 2013-12-22下午2:27:50
*/
public class ShortUrlUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 长连接： http://tech.sina.com.cn/i/2011-03-23/11285321288.shtml
		// 新浪解析后的短链接为： http://t.cn/h1jGSC
		String sLongUrl = "http://www.thinkphp.cn/extend/295.html";
		sLongUrl = "http://46.ut.com:8803/corebiz/sns/userarticle/articleviewentry.html?openpar=eyJpdGVtaWQiOiIyOTE2YWJiODY5MzE0MzM3OWUwNGM2MzQyM2NlM2FiOCIsInVzZXJpZCI6IjQ0NDg4YWUwMDhjMjRmN2ZhNGNlYWUwZDMwNjEzYzYwIn0=";

		System.out.println(getShortUrl(sLongUrl, "ut.cn", "http://"));
	}

	/**
	 * 获取短链接
	 * {@link #getShortUrl(String)}
	 * @param longUrl
	 * @param domainName
	 * @param protocol
	 * @return 短链接
	 */
	public static String getShortUrl(String longUrl, String domainName, String protocol) {

		/*StringBuilder shortUrl = new StringBuilder();
		shortUrl.append(protocol).append(domainName).append("/").append(getShortUrl(longUrl));*/
		return getShortUrl(longUrl);
	}

	/**
	 * 获取短链接
	 * {@link #getShortUrl(String[])}
	 * @param longUrl
	 * @return 短链接
	 */
	public static String getShortUrl(String longUrl) {

		return getShortUrl(createShortUrl(longUrl));
	}

	/**
	 * 在短链接数组中随机取一个
	 * @param shorUrl
	 * @return 短链接
	 */
	public static String getShortUrl(String[] shorUrl) {

		if (shorUrl.length != 4) {
			return null;
		}
		Random r = new Random();
		r.nextInt(4);// 在0,1,2,3四个数中随机产生一个
		return shorUrl[r.nextInt(4)];
	}
	/**
	 * 返回四组可做端连接的字符串，任选其一
	 * @param url
	 * @return 短链接数组
	 */
	public static String[] createShortUrl(String url) {

		// 可以自定义生成 MD5 加密字符传前的混合 KEY
		String key = "zhanghaili";
		// 要使用生成 URL 的字符
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
				"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1",
				"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };

		// 对传入网址进行 MD5 加密
		String sMD5EncryptResult = null;
		try {
			sMD5EncryptResult = CommonUtil.encodeMD5((key + url).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		String hex = sMD5EncryptResult;
		String[] resUrl = new String[4];
		for (int i = 0; i < 4; i++) {
			// 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
			String sTempSubString = hex.substring(i * 8, i * 8 + 8);
			// 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
			// long ，则会越界
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			for (int j = 0; j < 6; j++) {
				// 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
				long index = 0x0000003D & lHexLong;
				// 把取得的字符相加
				outChars += chars[(int) index];
				// 每次循环按位右移 5 位
				lHexLong = lHexLong >> 5;
			}

			// 把字符串存入对应索引的输出数组
			resUrl[i] = outChars;
		}
		return resUrl;
	}

}