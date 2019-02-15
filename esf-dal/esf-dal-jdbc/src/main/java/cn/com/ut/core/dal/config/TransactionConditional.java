package cn.com.ut.core.dal.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import cn.com.ut.core.common.util.CommonUtil;

public class TransactionConditional implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

		String group = context.getEnvironment().getProperty("ds.group[0]");
		return !CommonUtil.isEmpty(group);
	}

}
