package cn.com.ut.core.cache.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.ut.core.cache.CacheHelper;
import cn.com.ut.core.cache.annotation.Cached;
import cn.com.ut.core.common.constant.ConstantUtil;

@Component
@Aspect
public class CacheMethodInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(CacheMethodInterceptor.class);

	@Autowired(required = false)
	private CacheHelper cacheHelper;

	@Around("within(cn.com.ut.core.cache.MemcachedHelper)")
	public Object aroundCacheMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {

		if (CacheHelper.ENABLE_CACHE.equals(ConstantUtil.FLAG_NO)) {
			return null;
		} else {
			return pjp.proceed();
		}

	}

	@Around("within(cn.com.ut..service.impl.*ServiceImpl) && @annotation(cn.com.ut.core.cache.annotation.Cached)")
	public Object aroundServiceMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {

		Signature signature = pjp.getSignature();

		logger.debug(signature.toLongString());
		MethodSignature ms = (MethodSignature) signature;
		Object[] args = pjp.getArgs();
		Method method = ms.getMethod();
		Cached cached = ms.getMethod().getAnnotation(Cached.class);
		int exp = cached.expire();
		String key = cached.key();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i].length == 1
					&& annotations[i][0].annotationType().equals(Cached.class) && args[i] != null
					&& args[i] instanceof String) {
				key += "/" + String.valueOf(args[i]);
			}
		}

		logger.debug("key==" + key);

		Object result = cacheHelper.get(key);

		if (result != null) {
			return result;
		}

		result = pjp.proceed();

		if (result != null)
			cacheHelper.set(key, exp, result);

		return result;

	}

}
