package cn.com.ut.core.restful.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.RemoteProxyFailureException;
import org.springframework.remoting.support.RemotingSupport;
import org.springframework.util.Assert;

import cn.com.ut.core.restful.RequestData;
import cn.com.ut.core.restful.RestTemplateHelper;

public class RestInterceptor extends RemotingSupport
		implements MethodInterceptor, InitializingBean, ApplicationContextAware {

	private Object restProxy;

	private Class<?> serviceInterface;

	private String app;

	public String getApp() {

		return app;
	}

	public void setApp(String app) {

		this.app = app;
	}

	private RestTemplateHelper restTemplateHelper;

	public void setRestTemplateHelper(RestTemplateHelper restTemplateHelper) {

		this.restTemplateHelper = restTemplateHelper;
	}

	public void setServiceInterface(Class<?> serviceInterface) {

		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException("serviceInterface must be an interface");
		}
		this.serviceInterface = serviceInterface;
	}

	public Class<?> getServiceInterface() {

		return this.serviceInterface;
	}

	@Override
	public void afterPropertiesSet() {

		restProxy = createRestProxy();
	}

	protected Object createRestProxy() {

		Assert.notNull(getServiceInterface(), "serviceInterface is required");

		return Proxy.newProxyInstance(getBeanClassLoader(), new Class[] { getServiceInterface() },
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {

						RequestData requestData = RequestData.build();
						Class<?>[] parameterTypes = method.getParameterTypes();
						if (parameterTypes != null && parameterTypes.length > 0) {
							for (int i = 0; i < parameterTypes.length; i++) {
								requestData.addParamIndex(parameterTypes[i].getName(), args[i]);
							}
						}

						return restTemplateHelper.postForResponseData(null,
								getApp(), "/" + getApp() + "/rest/"
										+ getServiceInterface().getName() + "/" + method.getName(),
								requestData, true);
					}
				});

	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		if (this.restProxy == null)
			throw new IllegalStateException("RestInterceptor is not properly initialized");

		ClassLoader originalClassLoader = overrideThreadContextClassLoader();
		try {
			return invocation.getMethod().invoke(this.restProxy, invocation.getArguments());
		} catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			if (targetEx instanceof InvocationTargetException) {
				targetEx = ((InvocationTargetException) targetEx).getTargetException();
			}
			throw targetEx;
		} catch (Throwable ex) {
			throw new RemoteProxyFailureException("Failed to invoke rest proxy for remote service ["
					+ getServiceInterface().getName() + "]", ex);
		} finally {
			resetThreadContextClassLoader(originalClassLoader);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		RestTemplateHelper restTemplateHelper = applicationContext
				.getBean(RestTemplateHelper.class);
		Assert.notNull(restTemplateHelper, "restTemplateHelper is required");
		setRestTemplateHelper(restTemplateHelper);
	}

}
