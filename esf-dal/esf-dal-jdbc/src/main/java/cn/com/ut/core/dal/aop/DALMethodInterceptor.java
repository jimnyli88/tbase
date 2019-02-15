package cn.com.ut.core.dal.aop;

import java.lang.reflect.Method;
import java.util.LinkedList;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import cn.com.ut.core.dal.annotation.DAOMethod;
import cn.com.ut.core.dal.annotation.ServiceWrite;
import cn.com.ut.core.dal.jdbc.JdbcOperationsImpl;
import cn.com.ut.core.dal.jdbc.ThreadDataSource;

/**
 * DAO方法切面
 * 
 * @author wuxiaohua
 * @since 2015-12-30
 */
@Component
@Aspect
public class DALMethodInterceptor {

	private static ThreadLocal<LinkedList<ThreadDataSource>> threadDataSources = new NamedThreadLocal<LinkedList<ThreadDataSource>>(
			"DAO access resource");
	private static ThreadLocal<Integer> serviceAccessResource = new NamedThreadLocal<Integer>(
			"Service access resource");
	private static ThreadLocal<Boolean> serviceWrite = new NamedThreadLocal<Boolean>(
			"Service write");

	private static final Logger logger = LoggerFactory.getLogger(DALMethodInterceptor.class);

	public static void setThreadDataSource(ThreadDataSource threadDataSource) {

		if (threadDataSources.get() == null) {
			threadDataSources.set(new LinkedList<ThreadDataSource>());
		}
		threadDataSources.get().add(threadDataSource);
	}

	public static LinkedList<ThreadDataSource> getThreadDataSources() {

		return threadDataSources.get();
	}

	public static ThreadDataSource getThreadDataSource() {

		if (threadDataSources.get() == null || threadDataSources.get().isEmpty())
			return null;
		else
			return threadDataSources.get().getLast();
	}

	private static StringBuilder beforeDAOExp = new StringBuilder();
	private static StringBuilder aroundServiceExp = new StringBuilder();
	static {
		beforeDAOExp
				.append("(")
				.append("(within(cn.com.ut..dao.impl.*DAOImpl) || within(cn.com.ut.core.dal.jdbc.JdbcOperationsImpl))")
				.append(" && !within(cn.com.ut..log..dao.impl.*DAOImpl) && !within(cn.com.ut..resouce..dao.impl.*DAOImpl) && !within(cn.com.ut.sns..dao.impl.*DAOImpl)")
				.append(")");
		beforeDAOExp
				.append(" && (")
				.append("execution(* add*(..)) || execution(* find*(..))")
				.append(" || execution(* get*(..)) || execution(* update*(..)) || execution(* delete*(..))")
				.append(" || execution(* list*(..)) || execution(* count*(..))")
				.append(" || execution(* query*(..)) || @annotation(cn.com.ut.core.dal.annotation.DAOMethod)")
				.append(")");

		aroundServiceExp
				.append("within(cn.com.ut..service.impl.*ServiceImpl) && !within(cn.com.ut..log..service.impl.*)")
				.append(" && !within(cn.com.ut..resource..service.impl.*) && !within(cn.com.ut.sns..service.impl.*)");
	}

	/**
	 * DAO方法切面
	 * 
	 * @param jp
	 *            切面参数
	 */
	@Before("((within(*..dao.impl.*DAOImpl) || within(cn.com.ut.core.dal.jdbc.JdbcOperationsImpl))"
			+ " && !within(cn.com.ut..log..dao.impl.*DAOImpl)"
			+ " && !within(cn.com.ut..resouce..dao.impl.*DAOImpl)"
			+ " && !within(cn.com.ut.sns..dao.impl.*DAOImpl))")
	public void daoMethodBeforeAdvice(JoinPoint jp) {

		Object target = jp.getTarget();
		boolean b = JdbcOperationsImpl.class.isAssignableFrom(target.getClass());
		if (!b)
			return;
		Signature signature = jp.getSignature();
		logger.debug("dao method===" + signature.toLongString());
		String name = signature.getName();
		JdbcOperationsImpl object = (JdbcOperationsImpl) target;
		String groupName = object.getGroupName();

		MethodSignature ms = (MethodSignature) signature;

		boolean writeable = false;
		DAOMethod annotation = null;
		if (ms.getMethod().isAnnotationPresent(DAOMethod.class)) {
			annotation = ms.getMethod().getAnnotation(DAOMethod.class);
			if (!"".equals(annotation.group()))
				groupName = annotation.group();
			writeable = annotation.isWriteable();
		}

		if (serviceWrite.get() != null) {
			writeable = true;
		}

		logger.trace("groupName===" + groupName);

		ThreadDataSource threadDataSource = new ThreadDataSource(groupName, writeable);
		// 暂时关闭读写分离，总访问主库
		writeable = true;
		threadDataSource.setWriteable(writeable);

		if (!writeable) {

			if (name.startsWith("add") || name.startsWith("insert") || name.startsWith("delete")
					|| name.startsWith("update") || name.endsWith("_") || name.startsWith("count")
					|| name.startsWith("checkUnique")) {

				threadDataSource.setWriteable(true);
			} else if (name.startsWith("get") || name.startsWith("query")
					|| name.startsWith("list")) {

				if (getThreadDataSources() != null) {
					int index = -1;
					if ((index = getThreadDataSources().lastIndexOf(threadDataSource)) != -1) {
						ThreadDataSource other = getThreadDataSources().get(index);
						if (other.isWriteable())
							threadDataSource.setWriteable(true);
					}

				}

			} else {
				// 默认Writeable == true
				threadDataSource.setWriteable(true);
			}
		}

		setThreadDataSource(threadDataSource);

		logger.trace(getThreadDataSources().toString());
		logger.trace("dao method end===" + signature.toLongString());
	}

	/**
	 * 业务方法切面
	 * 
	 * @param pjp
	 *            切面参数
	 * @return 方法调用返回值
	 * @throws Throwable
	 */
	@Around("within(*..service.impl.*ServiceImpl) && !within(cn.com.ut..log..service.impl.*)"
			+ " && !within(cn.com.ut..resource..service.impl.*) && !within(cn.com.ut.sns..service.impl.*)")
	public Object aroundServiceMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {

		Signature signature = pjp.getSignature();
		MethodSignature ms = (MethodSignature) signature;
		logger.debug("service method===" + signature.toLongString());
		Boolean isWrite = serviceWrite.get();
		if (isWrite == null) {
			if (ms.getMethod().isAnnotationPresent(ServiceWrite.class)) {
				serviceWrite.set(Boolean.TRUE);
			}
		}
		Integer counter = serviceAccessResource.get();
		if (counter == null)
			serviceAccessResource.set(new Integer(1));
		else
			serviceAccessResource.set(++counter);

		try {
			return pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			threadDataSources.remove();
			serviceAccessResource.remove();
			throw e;
		} finally {
			counter = serviceAccessResource.get();
			if (counter != null) {
				if (counter == 1) {
					threadDataSources.remove();
					serviceAccessResource.remove();
					serviceWrite.remove();
				} else {
					serviceAccessResource.set(--counter);
				}
			}
			logger.trace("service method end===" + signature.toLongString());
			logger.trace("service resource==" + serviceAccessResource.get());
			logger.trace("dao resource==" + threadDataSources.get());
		}

	}

	/**
	 * 反射获取方法对象
	 * 
	 * @param target
	 *            目标类
	 * @param name
	 *            方法名称
	 * @param parameters
	 *            参数类型
	 * @return 方法对象
	 * @throws NoSuchMethodException
	 */
	private Method findMethodFromTargetGivenNameAndParams(final Object target, final String name,
			final Class<?>[] parameters) throws NoSuchMethodException {

		Method method = target.getClass().getMethod(name, parameters);
		logger.debug("Method to cache: {}", method);

		return method;
	}

	/**
	 * 切面获取方法对象
	 * 
	 * @param jp
	 *            连接点
	 * @return 放对象
	 * @throws NoSuchMethodException
	 */
	public Method getMethod(final JoinPoint jp) throws NoSuchMethodException {

		final Signature sig = jp.getSignature();
		if (!(sig instanceof MethodSignature)) {
			throw new RuntimeException("This annotation is only valid on a method.");
		}

		final MethodSignature msig = (MethodSignature) sig;
		final Object target = jp.getTarget();

		// cannot use msig.getMethod() because it can return the method where
		// annotation was declared i.e. method in interface
		String name = msig.getName();
		Class<?>[] parameters = msig.getParameterTypes();
		Method method = findMethodFromTargetGivenNameAndParams(target, name, parameters);
		return method;
	}
}
