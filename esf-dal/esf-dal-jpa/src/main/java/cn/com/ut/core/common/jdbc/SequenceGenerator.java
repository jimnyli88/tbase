package cn.com.ut.core.common.jdbc;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import cn.com.ut.core.common.util.GIDGenerator;

/**
 * @author wuxiaohua
 * @since 2018/10/29
 */
public class SequenceGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object)
			throws HibernateException {

		return String.valueOf(GIDGenerator.getInstance().nextId());
	}
}
