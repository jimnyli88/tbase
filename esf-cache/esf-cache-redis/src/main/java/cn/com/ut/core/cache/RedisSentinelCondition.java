package cn.com.ut.core.cache;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RedisSentinelCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

		Environment env = context.getEnvironment();
		if (env == null)
			return false;
		if ("sentinel".equals(env.getProperty("spring.redis.mode")))
			return true;
		return false;

	}
}
