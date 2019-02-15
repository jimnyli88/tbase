package cn.com.ut.core.dal.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ds")
public class DataSourceAutoConfig {

	private List<String> group;
	private Map<String, String> url;
	private Map<String, String> username;
	private Map<String, String> password;

	public Map<String, String> getUrl() {

		return url;
	}

	public void setUrl(Map<String, String> url) {

		this.url = url;
	}

	public Map<String, String> getUsername() {

		return username;
	}

	public void setUsername(Map<String, String> username) {

		this.username = username;
	}

	public Map<String, String> getPassword() {

		return password;
	}

	public void setPassword(Map<String, String> password) {

		this.password = password;
	}

	public List<String> getGroup() {

		return group;
	}

	public void setGroup(List<String> group) {

		this.group = group;
	}

	

}
