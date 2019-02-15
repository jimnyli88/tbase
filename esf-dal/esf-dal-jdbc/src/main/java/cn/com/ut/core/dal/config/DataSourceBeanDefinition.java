package cn.com.ut.core.dal.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

import cn.com.ut.core.common.util.CollectionUtil;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.dal.datasource.DataSourceGroup;
import cn.com.ut.core.dal.datasource.DataSourceRouter;
import cn.com.ut.core.dal.datasource.DataSourceShard;
import cn.com.ut.core.dal.datasource.DataSourceWrapper;
import cn.com.ut.core.dal.transaction.ChainedTransactionManager;

@Component
public class DataSourceBeanDefinition implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceBeanDefinition.class);

	@Autowired
	private DataSourceAutoConfig dataSourceAutoConfig;

	List<PlatformTransactionManager> txc = new ArrayList<>();

	public List<PlatformTransactionManager> getTxc() {

		return txc;
	}

	public void setTxc(List<PlatformTransactionManager> txc) {

		this.txc = txc;
	}

	private BeanDefinition defaultBeanDefinition(Class<?> beanClass, Map<?, ?> propertyValues) {

		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(beanClass);
		definition.setScope(BeanDefinition.SCOPE_SINGLETON);
		definition.setLazyInit(false);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		definition.setAutowireCandidate(true);
		if (propertyValues != null) {
			MutablePropertyValues mp = new MutablePropertyValues();
			mp.addPropertyValues(propertyValues);
			definition.setPropertyValues(mp);
		}
		return definition;

	}

	private List<String> urlGroup(String groupName) {

		List<String> urlGroup = new ArrayList<String>();
		if (!CollectionUtil.isEmptyMap(dataSourceAutoConfig.getUrl())) {

			Set<Map.Entry<String, String>> entrySet = dataSourceAutoConfig.getUrl().entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				if (entry.getKey().startsWith(groupName + ".")) {
					if (entry.getKey().endsWith(".0")) {
						urlGroup.add(0, entry.getValue());
					} else {
						urlGroup.add(entry.getValue());
					}
				}
			}
		}
		return urlGroup;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {

		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx
				.getAutowireCapableBeanFactory();
		// TODO Auto-generated method stub
		dataSourceAutoConfig = ctx.getBean(DataSourceAutoConfig.class);

		if (CollectionUtil.isEmptyCollection(dataSourceAutoConfig.getGroup()))
			return;

		Map<String, DataSourceGroup> router = new HashMap<>();

		for (String groupName : dataSourceAutoConfig.getGroup()) {

			List<String> urlGroup = urlGroup(groupName);
			if (CollectionUtil.isEmptyCollection(urlGroup))
				continue;

			else {

				Map<String, Object> groupVo = new HashMap<>();
				groupVo.put("groupName", groupName + "G");
				Map<String, Object> shardVo = new HashMap<>();
				shardVo.put("shardName", groupName + ".shard");
				List<DataSourceWrapper> slaves = new ArrayList<>();

				for (int i = 0; i < urlGroup.size(); i++) {

					Map<String, Object> dataSourceVo = new HashMap<>();

					String dbUrl = urlGroup.get(i);
					if (dbUrl.startsWith("jdbc:postgresql")) {
						dataSourceVo.put("driverClassName", "org.postgresql.Driver");
					} else {
						dataSourceVo.put("driverClassName", "com.mysql.jdbc.Driver");
					}
					dataSourceVo.put("name", groupName + ".ds." + i);
					if (CollectionUtil.isEmptyMap(dataSourceAutoConfig.getUsername()) || CommonUtil
							.isEmpty(dataSourceAutoConfig.getUsername().get(groupName))) {
						dataSourceVo.put("username",
								dataSourceAutoConfig.getUsername().get("default"));
					}
					if (CollectionUtil.isEmptyMap(dataSourceAutoConfig.getPassword()) || CommonUtil
							.isEmpty(dataSourceAutoConfig.getPassword().get(groupName))) {
						dataSourceVo.put("password",
								dataSourceAutoConfig.getPassword().get("default"));
					}
					dataSourceVo.put("url", dbUrl);

					BeanDefinition dataSourceBD = defaultBeanDefinition(DruidDataSource.class,
							dataSourceVo);
					((GenericBeanDefinition) dataSourceBD).setDestroyMethodName("close");
					defaultListableBeanFactory.registerBeanDefinition(groupName + ".ds." + i,
							dataSourceBD);
					DruidDataSource dsObj = (DruidDataSource) ctx.getBean(groupName + ".ds." + i);

					logger.trace("====ds:" + groupName + ".ds." + i);

					Map<String, Object> jdbcVo = new HashMap<>();
					jdbcVo.put("dataSource", dsObj);
					BeanDefinition jdbcBD = defaultBeanDefinition(JdbcTemplate.class, jdbcVo);
					defaultListableBeanFactory.registerBeanDefinition(groupName + ".jdbc." + i,
							jdbcBD);

					JdbcTemplate jdbcObj = (JdbcTemplate) ctx.getBean(groupName + ".jdbc." + i);
					logger.trace("====jdbc:" + groupName + ".jdbc." + i);

					Map<String, Object> dswVo = new HashMap<>();
					dswVo.put("dataSourceName", groupName + ".ds." + i);
					dswVo.put("jdbcTemplate", jdbcObj);
					BeanDefinition dswBD = defaultBeanDefinition(DataSourceWrapper.class, dswVo);
					defaultListableBeanFactory.registerBeanDefinition(groupName + ".dsw." + i,
							dswBD);
					DataSourceWrapper dswObj = (DataSourceWrapper) ctx
							.getBean(groupName + ".dsw." + i);

					if (i == 0) {
						Map<String, Object> txVo = new HashMap<>();
						txVo.put("dataSource", dsObj);
						BeanDefinition txBD = defaultBeanDefinition(
								DataSourceTransactionManager.class, txVo);
						defaultListableBeanFactory.registerBeanDefinition(groupName + ".tx", txBD);
						DataSourceTransactionManager txObj = (DataSourceTransactionManager) ctx
								.getBean(groupName + ".tx");
						txc.add(txObj);

						shardVo.put("master", dswObj);
					} else {
						slaves.add(dswObj);
					}
				}

				if (!slaves.isEmpty()) {
					shardVo.put("slaves", slaves);
				}

				BeanDefinition shardBD = defaultBeanDefinition(DataSourceShard.class, shardVo);
				defaultListableBeanFactory.registerBeanDefinition(groupName + ".shard", shardBD);
				DataSourceShard shardObj = (DataSourceShard) ctx.getBean(groupName + ".shard");

				Map<String, DataSourceShard> shards = new java.util.LinkedHashMap<String, DataSourceShard>();
				shards.put(groupName + ".shard", shardObj);
				groupVo.put("shards", shards);
				BeanDefinition groupBD = defaultBeanDefinition(DataSourceGroup.class, groupVo);
				defaultListableBeanFactory.registerBeanDefinition(groupName + "G", groupBD);
				DataSourceGroup groupObj = (DataSourceGroup) ctx.getBean(groupName + "G");

				router.put(groupName + "G", groupObj);
			}

		}

		Map<String, Object> routerVo = new HashMap<>();
		routerVo.put("groups", router);
		BeanDefinition routerBD = defaultBeanDefinition(DataSourceRouter.class, routerVo);
		((GenericBeanDefinition) routerBD).setInitMethodName("init");
		((GenericBeanDefinition) routerBD).setDestroyMethodName("destroy");
		defaultListableBeanFactory.registerBeanDefinition("router", routerBD);
		DataSourceRouter routerObj = (DataSourceRouter) ctx.getBean("router");

		// BeanDefinition txcBD =
		// defaultBeanDefinition(ChainedTransactionManager.class, null);
		// txcBD.setPrimary(true);
		// txcBD.getConstructorArgumentValues()
		// .addGenericArgumentValue(txc.toArray(new
		// PlatformTransactionManager[txc.size()]));
		// defaultListableBeanFactory.registerBeanDefinition("transactionManager",
		// txcBD);
		// ChainedTransactionManager txcObj = (ChainedTransactionManager) ctx
		// .getBean("transactionManager");

	}

}
