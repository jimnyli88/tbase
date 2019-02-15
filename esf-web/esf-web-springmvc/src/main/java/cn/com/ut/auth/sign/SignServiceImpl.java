package cn.com.ut.auth.sign;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.com.ut.auth.exception.AuthException;
import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.CommonUtil;

/**
 * 验证签名工具类
 * 
 * @author wuxiaohua
 * @since 2017年10月10日
 */
@Configuration
@ConfigurationProperties(prefix = "auth")
public class SignServiceImpl implements SignService {

	private List<String> signTypes = Arrays.asList(new String[] { "MD5" });

	private String appid;
	private String appkey;

	public String getAppid() {

		return appid;
	}

	public void setAppid(String appid) {

		this.appid = appid;
	}

	public String getAppkey() {

		return appkey;
	}

	public void setAppkey(String appkey) {

		this.appkey = appkey;
	}

	/**
	 * 对返回数据进行签名
	 * 
	 * @param toMap
	 */
	@Override
	public void signTo(Map<String, Object> toMap) {

		toMap.put("signtype", "MD5");
		toMap.put("appid", appid);
		toMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		String queryParam = mapToQueryParam(toMap, new String[] { "sign" });
		String toSign = sign((String) toMap.get("appid"), queryParam,
				(String) toMap.get("signtype"));
		toMap.put("sign", toSign);
	}

	/**
	 * 对数据体进行签名
	 * 
	 * @param fromMap
	 * @return
	 */
	@Override
	public String signGen(Map<String, Object> fromMap) {

		if (CollectionUtil.isEmptyMap(fromMap))
			throw new AuthException();

		String signType = (String) fromMap.get("signtype");
		String appId = (String) fromMap.get("appid");
		if (CommonUtil.isEmpty(fromMap, new String[] { "signtype", "appid", "timestamp" })) {
			throw new AuthException();
		}

		if (!signTypes.contains(signType)) {
			throw new AuthException();
		}

		String queryParam = mapToQueryParam(fromMap, new String[] { "sign" });
		return sign(appId, queryParam, signType);

	}

	/**
	 * 对请求数据进行签名验证
	 * 
	 * @param fromMap
	 * @return
	 */
	@Override
	public boolean signFromVerify(Map<String, Object> fromMap) {

		if (CollectionUtil.isEmptyMap(fromMap))
			throw new AuthException();

		String fromSign = (String) fromMap.get("sign");
		String signType = (String) fromMap.get("signtype");
		String appId = (String) fromMap.get("appid");
		if (CommonUtil.isEmpty(fromMap,
				new String[] { "sign", "signtype", "appid", "timestamp" })) {
			throw new AuthException();
		}

		if (!signTypes.contains(signType)) {
			throw new AuthException();
		}

		String queryParam = mapToQueryParam(fromMap, new String[] { "sign" });
		String toSign = sign(appId, queryParam, signType);
		return fromSign.equals(toSign);

	}

	/**
	 * 将参数键值对转换为字符串拼接形式
	 * 
	 * @param fromMap
	 * @param excludeKeys
	 * @return
	 */
	public String mapToQueryParam(Map<String, Object> fromMap, String[] excludeKeys) {

		if (CollectionUtil.isEmptyMap(fromMap))
			return "";

		Map<String, String> toMap = new TreeMap<String, String>();
		Set<Entry<String, Object>> entrySet = fromMap.entrySet();

		List<String> excludeKeyList = null;
		if (excludeKeys != null && excludeKeys.length > 0) {

			excludeKeyList = Arrays.asList(excludeKeys);
		}

		for (Entry<String, Object> entry : entrySet) {

			if (entry.getValue() != null && !CommonUtil.isEmpty(entry.getValue().toString())
					&& !excludeKeyList.contains(entry.getKey())) {

				toMap.put(entry.getKey(), entry.getValue().toString());
			}
		}

		StringBuilder sb = new StringBuilder();
		if (!toMap.isEmpty()) {

			sb = new StringBuilder();
			Set<Entry<String, String>> toEntrySet = toMap.entrySet();
			for (Entry<String, String> toEntry : toEntrySet) {
				if (sb.length() == 0) {
					sb.append(toEntry.getKey()).append("=").append(toEntry.getValue());
				} else {
					sb.append("&").append(toEntry.getKey()).append("=").append(toEntry.getValue());
				}
			}

		}

		return sb.toString();
	}

	public String getAppKey(String appId) {

		if (appId.equals(appid)) {
			return appkey;
		} else {
			return null;
		}
	}

	/**
	 * 生产签名
	 * 
	 * @param appId
	 * @param queryParam
	 * @param signType
	 * @return
	 */
	public String sign(String appId, String queryParam, String signType) {

		if (CommonUtil.isEmpty(appId))
			throw new AuthException("appId");
		String appKey = getAppKey(appId);
		if (CommonUtil.isEmpty(appKey))
			throw new AuthException("appKey");
		if (CommonUtil.isEmpty(queryParam))
			throw new AuthException("queryParam");
		queryParam += "&appkey=" + appKey;
		return CommonUtil.encodeMD5(queryParam.getBytes()).toUpperCase();

	}
}
