package cn.com.ut.core.dal.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import cn.com.ut.core.dal.annotation.MycatRule;
import cn.com.ut.core.dal.annotation.ShardRule;
import cn.com.ut.core.dal.beans.DAORoute;
import cn.com.ut.core.dal.beans.ServiceRoute;
import cn.com.ut.core.dal.jdbc.JdbcOperationsImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * DAO方法切面
 * 
 * @author wuxiaohua
 * @since 2017年8月1日
 */
@Component
@Aspect
@Slf4j
public class DALMethodInterceptor {

	private static ThreadLocal<DAORoute> daoRouteThreadLocal = new NamedThreadLocal<DAORoute>(
			"DAO route");

	private static ThreadLocal<ServiceRoute> serviceRouteThreadLocal = new NamedThreadLocal<ServiceRoute>(
			"Service route");

	public static ThreadLocal<DAORoute> getDaoRouteThreadLocal() {

		return daoRouteThreadLocal;
	}

	public static ThreadLocal<ServiceRoute> getServiceRouteThreadLocal() {

		return serviceRouteThreadLocal;
	}

	private static StringBuilder beforeDAOExp = new StringBuilder();
	private static StringBuilder aroundServiceExp = new StringBuilder();
	static {
		beforeDAOExp.append("(")
				.append("(within(cn.com.ut..dao.impl.*DAOImpl) || within(cn.com.ut.core.dal.jdbc.JdbcOperationsImpl))")
				.append(" && !within(cn.com.ut..log..dao.impl.*DAOImpl) && !within(cn.com.ut..resouce..dao.impl.*DAOImpl) && !within(cn.com.ut.sns..dao.impl.*DAOImpl)")
				.append(")");
		beforeDAOExp.append(" && (").append("execution(* add*(..)) || execution(* find*(..))")
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
	@Before("((within(cn.com.ut..dao.impl.*DAOImpl) || within(cn.com.ut.core.dal.jdbc.JdbcOperationsImpl))"
			+ " && !within(cn.com.ut..log..dao.impl.*DAOImpl)"
			+ " && !within(cn.com.ut..resouce..dao.impl.*DAOImpl)"
			+ " && !within(cn.com.ut.sns..dao.impl.*DAOImpl))")
	public void daoMethodBeforeAdvice(JoinPoint jp) {

		Object target = jp.getTarget();
		boolean b = JdbcOperationsImpl.class.isAssignableFrom(target.getClass());
		if (!b)
			return;
		Signature signature = jp.getSignature();
		log.debug("dao method===" + signature.toLongString());
		String name = signature.getName();
		JdbcOperationsImpl dao = (JdbcOperationsImpl) target;
		String dataNode = dao.getGroupName();
		Class<?> entity = dao.getEntity();

		MethodSignature ms = (MethodSignature) signature;

		if (entity.isAnnotationPresent(ShardRule.class)
				&& entity.getAnnotation(ShardRule.class).shard()) {
			dataNode = null;
		}

		log.trace("dataNode===" + dataNode);

		DAORoute daoRoute = daoRouteThreadLocal.get();
		if (daoRoute == null) {
			daoRoute = new DAORoute();
		}
		daoRoute.setDataNode(dataNode);

		ServiceRoute serviceRoute = serviceRouteThreadLocal.get();
		if (!serviceRoute.getServiceSignature().equals(daoRoute.getServiceSignature())) {
			daoRoute.setDaoIndex(1);
		} else {
			daoRoute.setDaoIndex(daoRoute.getDaoIndex() + 1);
		}

		daoRouteThreadLocal.set(daoRoute);

		if (!serviceRoute.isMaster()) {

			if (name.startsWith("add") || name.startsWith("insert") || name.startsWith("delete")
					|| name.startsWith("update") || name.endsWith("_") || name.startsWith("count")
					|| name.startsWith("checkUnique")) {

				serviceRoute.setMaster(true);
				serviceRouteThreadLocal.set(serviceRoute);
			} else if (name.startsWith("get") || name.startsWith("query")
					|| name.startsWith("list")) {

			} else {
				// 默认Write == true
				serviceRoute.setMaster(true);
				serviceRouteThreadLocal.set(serviceRoute);
			}
		}

		log.trace("dao method end===" + signature.toLongString());
	}

	/**
	 * 业务方法切面
	 * 
	 * @param pjp
	 *            切面参数
	 * @return 方法调用返回值
	 * @throws Throwable
	 */
	@Around("within(cn.com.ut..service.impl.*ServiceImpl) && !within(cn.com.ut..log..service.impl.*)"
			+ " && !within(cn.com.ut..resource..service.impl.*) && !within(cn.com.ut.sns..service.impl.*)")
	public Object aroundServiceMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {

		Signature signature = pjp.getSignature();
		MethodSignature ms = (MethodSignature) signature;
		log.debug("service method===" + signature.toLongString());

		ServiceRoute serviceRoute = serviceRouteThreadLocal.get();
		if (serviceRoute == null) {
			serviceRoute = new ServiceRoute();
		} else {
			serviceRoute.setDepth(serviceRoute.getDepth() + 1);
		}

		serviceRoute.setServiceSignature(signature.toLongString());

		if (ms.getMethod().isAnnotationPresent(MycatRule.class)) {

			MycatRule mycatRule = ms.getMethod().getAnnotation(MycatRule.class);
			if (mycatRule.xa() && !serviceRoute.isXa()) {
				serviceRoute.setXa(true);
			}
			if (mycatRule.master() && !serviceRoute.isMaster()) {
				serviceRoute.setMaster(true);
			}
		}

		serviceRouteThreadLocal.set(serviceRoute);

		try {
			return pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			daoRouteThreadLocal.remove();
			serviceRouteThreadLocal.remove();
			throw e;
		} finally {
			if (serviceRoute.getDepth() == 1) {
				daoRouteThreadLocal.remove();
				serviceRouteThreadLocal.remove();
			} else {
				serviceRoute.setDepth(serviceRoute.getDepth() - 1);
				serviceRouteThreadLocal.set(serviceRoute);
			}

			log.trace("service method end===" + signature.toLongString());
			log.trace("service route==" + serviceRouteThreadLocal.get());
			log.trace("dao route==" + daoRouteThreadLocal.get());
		}
	}

}
