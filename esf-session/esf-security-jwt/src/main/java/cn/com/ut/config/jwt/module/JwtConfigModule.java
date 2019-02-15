package cn.com.ut.config.jwt.module;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedCaseInsensitiveMap;

@ConfigurationProperties(prefix = "jwt")
public class JwtConfigModule {

	private String secretkey;

	private Long afterSecondCanRefresh;

	private Map<String, Map<String, Long>> expiration = new LinkedCaseInsensitiveMap<>();

	public String getSecretkey() {

		return secretkey;
	}

	public void setSecretkey(String secretkey) {

		this.secretkey = secretkey;
	}

	public Map<String, Map<String, Long>> getExpiration() {

		return expiration;
	}

	public void setExpiration(Map<String, Map<String, Long>> expiration) {

		this.expiration = expiration;
	}

	public Long getAfterSecondCanRefresh() {

		return afterSecondCanRefresh;
	}

	public void setAfterSecondCanRefresh(Long afterSecondCanRefresh) {

		this.afterSecondCanRefresh = afterSecondCanRefresh;
	}
}
