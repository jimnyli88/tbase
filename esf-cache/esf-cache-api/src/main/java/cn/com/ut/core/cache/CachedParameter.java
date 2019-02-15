package cn.com.ut.core.cache;

/**
 * 缓存参数
 * 
 * @author wuxiaohua
 * @since 2014-3-18
 */
public class CachedParameter {

	private String key;
	private int expire = CacheHelper.DEFAULT_EXPIRE;
	private boolean cacheable = false;
	private String[] evict;

	public CachedParameter() {

	}

	public CachedParameter(boolean cacheable) {

		this.cacheable = cacheable;
	}

	public CachedParameter(boolean cacheable, String[] evict) {

		this(cacheable);
		this.evict = evict;
	}

	public CachedParameter(String key, int expire, boolean cacheable, String[] evict) {

		this(cacheable, evict);
		this.key = key;
		this.expire = expire;
	}

	public String[] getEvict() {

		return evict;
	}

	public void setEvict(String[] evict) {

		this.evict = evict;
	}

	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

	public int getExpire() {

		return expire;
	}

	public void setExpire(int expire) {

		this.expire = expire;
	}

	public boolean isCacheable() {

		return cacheable;
	}

	public void setCacheable(boolean cacheable) {

		this.cacheable = cacheable;
	}

}
