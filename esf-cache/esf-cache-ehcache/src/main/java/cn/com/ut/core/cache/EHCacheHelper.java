package cn.com.ut.core.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public interface EHCacheHelper {

	CacheManager getCacheManager();

	Cache getCache();

	void put(Element element);

	boolean remove(String key);

	Element getQuiet(String key);

	<T> T getQuiet(String key, Class<T> clazz);

	Element replace(Element element);

}