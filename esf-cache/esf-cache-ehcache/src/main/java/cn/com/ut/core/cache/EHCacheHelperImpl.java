package cn.com.ut.core.cache;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component("ehCacheHelper")
public class EHCacheHelperImpl implements InitializingBean, DisposableBean, EHCacheHelper {

	private static final String CACHE_NAME = "cache";

	@Autowired(required = false)
	private CacheManager cacheManager;
	private Cache cache;

	/**
	 * 缓存总管理实例，可管理多个缓存实例
	 */
	public CacheManager getCacheManager() {

		return cacheManager;
	}

	/**
	 * 缓存实例
	 */
	public Cache getCache() {

		return cache;
	}

	/**
	 * 初始化，构建缓存实例
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		// List<URL> urls =
		// FileUtil.listUrls("cn/com/ut/config/ehcache/ehcache.xml", null);
		//
		// if (urls != null && !urls.isEmpty())
		// cacheManager = new CacheManager(urls.iterator().next());

		if (cacheManager != null && cacheManager.cacheExists(CACHE_NAME))
			cache = cacheManager.getCache(CACHE_NAME);

	}

	/**
	 * 资源释放
	 */
	@Override
	public void destroy() throws Exception {

		if (cache != null) {
			cache.removeAll();
			cache.dispose();
			cache = null;
		}

		if (cacheManager != null) {
			cacheManager.removeAllCaches();
			cacheManager.shutdown();
			cacheManager = null;
		}

	}

	/**
	 * 设置缓存
	 * 
	 * @param element
	 *            缓存数据
	 */
	public void put(Element element) {

		getCache().put(element);
	}

	/**
	 * 移除缓存
	 * 
	 * @param key
	 *            缓存数据key
	 * @return 操作是否成功
	 */
	public boolean remove(String key) {

		return getCache().remove(key);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 *            缓存数据key
	 * @return 缓存数据
	 */
	public final Element get(String key) {

		return getCache().get(key);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 *            缓存数据key
	 * @return 缓存数据
	 */
	public final Element getQuiet(String key) {

		return getCache().getQuiet(key);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 *            缓存数据key
	 * @param clazz
	 *            返回类型
	 * @return 缓存数据实例
	 */
	@SuppressWarnings("unchecked")
	public <T> T getQuiet(String key, Class<T> clazz) {

		Element e = getCache().getQuiet(key);
		if (e == null)
			return null;
		Object value = e.getObjectValue();
		return value == null ? null : (T) value;
	}

	/**
	 * 替换缓存数据
	 * 
	 * @param element
	 *            缓存数据
	 * @param clazz
	 *            返回类型
	 * @return 缓存数据
	 */
	public Element replace(Element element) {

		return getCache().replace(element);

	}

}
