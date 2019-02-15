package cn.com.ut.core.net;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * 主机地址
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:27:50
 */
public class HostAddress implements Serializable, Comparable<HostAddress> {

	private static final long serialVersionUID = -4930707622859969060L;
	/**
	 * ip
	 */
	private String host;
	/**
	 * service type
	 */
	private String serviceType;
	/**
	 * port
	 */
	private String port;

	/**
	 * 
	 * @param host
	 * @param port
	 * @param serviceType
	 */
	public HostAddress(String host, String port, String serviceType) {

		this.host = host;
		this.port = port;
		this.serviceType = serviceType;

	}

	public String getPort() {

		return port;
	}

	public void setPort(String port) {

		this.port = port;
	}

	public String getHost() {

		return host;
	}

	public void setHost(String host) {

		this.host = host;
	}

	public String getServiceType() {

		return serviceType;
	}

	public void setServiceType(String serviceType) {

		this.serviceType = serviceType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostAddress other = (HostAddress) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HostAddress other) {

		HostAddress otherHost = (HostAddress) other;
		return new CompareToBuilder().append(this.host, otherHost.host)
				.append(this.serviceType, otherHost.serviceType).toComparison();
	}

}
