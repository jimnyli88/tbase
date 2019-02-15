package cn.com.ut.core.restful.rpc;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

public class RestProxyFactoryBean extends RestInterceptor implements FactoryBean<Object> {

	private Object serviceProxy;

	@Override
	public void afterPropertiesSet() {

		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this)
				.getProxy(getBeanClassLoader());
	}

	@Override
	public Object getObject() {

		return this.serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {

		return getServiceInterface();
	}

	@Override
	public boolean isSingleton() {

		return true;
	}
}
