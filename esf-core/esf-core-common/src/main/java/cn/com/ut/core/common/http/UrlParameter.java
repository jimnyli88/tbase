package cn.com.ut.core.common.http;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
/**
* url参数
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class UrlParameter {

	public static void main(String[] args) throws Exception {

		String url = "/system/FileLoad?group=fdf9788418904dc9a7dd7dd6179ea68f&amp;height=348&amp;width=348&amp;aa=&amp;";
		System.out.println(parseParameter(url));
	}

	/**
	 *
	 * @param url
	 * @return new url
	 * @throws IOException
	 */
	public static String parseHost(String url) throws IOException {

		URL r = new URL(url);
		return r.getProtocol() + "://" + r.getAuthority();
	}

	/**
	 * 解析url参数，将url参数以map集合返回
	 * @param url
	 * @return 返回url参数map集合
	 */
	public static Map<String, String> parseParameter(String url) {

		Map<String, String> map = new LinkedHashMap<String, String>();

		url = StringEscapeUtils.unescapeHtml4(url);
		int index = url.indexOf("?");
		if (index != -1 && index < url.length() - 1) {
			String sub = url.substring(index + 1);
			String[] items = sub.split("&");
			for (String item : items) {
				String[] ps = item.split("=");
				if (ps.length == 2) {
					String name = ps[0].trim();
					if (!"".equals(name))
						map.put(name, ps[1]);
				} else if (ps.length == 1) {
					String name = ps[0].trim();
					if (!"".equals(name))
						map.put(name, "");
				}
			}
		}

		return map;
	}

}
