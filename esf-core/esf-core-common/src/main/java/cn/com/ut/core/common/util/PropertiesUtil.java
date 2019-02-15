package cn.com.ut.core.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.ClassPathResource;
/**
* Properties工具类
* @author wuxiaohua
* @since 2013-12-22下午2:17:45
*/
public class PropertiesUtil {

	/**
	 * 系统常用配置路径
	 */
	private static final String PATH = "cn/com/ut/config/properties/constant.properties";
	/**
	 * 系统配置路径数组
	 */
	private static final String[] PROPERTIES = new String[] { PATH };
	/**
	 * 存放系统每个配置对应的Properties
	 */
	private static Map<String, Properties> map = new ConcurrentHashMap<String, Properties>();

	/**
	 * 初始化系统配置路径对应的Properties
	 */
	static {

		for (String p : PROPERTIES) {
			try {
				Properties properties = new Properties();
				ClassPathResource resource = new ClassPathResource(p);
				InputStream in = resource.getInputStream();
				properties.load(in);
				map.put(p, properties);
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 在指定的配置路径下获取指定key的value
	 * @param key
	 * @param p
	 * @return 返回指定key对应的value
	 */
	public static String getProperty(String key, String p) {

		Properties properties = null;
		if ((properties = map.get(p)) == null) {
			try {
				ClassPathResource resource = new ClassPathResource(p);
				InputStream in = resource.getInputStream();
				properties = new Properties();
				properties.load(in);
				synchronized (PropertiesUtil.class) {
					if (map.get(p) == null)
						map.put(p, properties);
				}
			} catch (IOException e) {
			}
		}
		return properties == null ? null : properties.getProperty(key);
	}

	/**
	 *
	 * @param key
	 * @return 返回指定key对应的value
	 */
	public static String getProperty(String key) {

		return getProperty(key, PATH);

	}

}
