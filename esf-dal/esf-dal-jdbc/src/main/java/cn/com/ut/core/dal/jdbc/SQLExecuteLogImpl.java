package cn.com.ut.core.dal.jdbc;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SQLExecuteLogImpl implements SQLExecuteLog {

	private static final Logger logger = LoggerFactory.getLogger(SQLExecuteLogImpl.class);

	/**
	 * 
	 * @see cn.com.ut.core.dal.jdbc.SQLExecuteLog#dataAccessLog(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void dataAccessLog(String sql, Object[] array) {

		if (sql != null)
			logger.debug("sql=={}", sql);
		if (array != null)
			logger.debug("array=={}", Arrays.asList(array));
	}
}
