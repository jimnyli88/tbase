package cn.com.ut.core.dal.datasource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.constant.DictionaryConstant;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.PropertiesUtil;
import cn.com.ut.core.dal.aop.DALMethodInterceptor;
import cn.com.ut.core.dal.jdbc.ThreadDataSource;

/**
 * 数据源分组管理
 * 
 * @author wuxiaohua
 * @since 2014-3-11
 */
public class DataSourceRouter {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceRouter.class);
	
	/**
	 * datasource ping开关
	 */
	public static final String DATASOURCE_PING_OPEN_PROPERTIES = "datasource.ping.open";

	/**
	 * 数据源分组
	 */
	private static Map<String, DataSourceGroup> groups = new LinkedHashMap<String, DataSourceGroup>();

	/**
	 * 数据源包装器
	 */
	private List<DataSourceWrapper> dataSourceWrapperList = new ArrayList<DataSourceWrapper>();

	private static ReentrantLock lock = new ReentrantLock();

	public static final byte STRATEGY_ORDER = 0x01;
	public static final byte STRATEGY_WEIGHT = 0x02;
	public static byte strategy = STRATEGY_WEIGHT;

	public static Random random = new Random();

	public static AtomicInteger counter = new AtomicInteger(0);

	private ScheduledExecutorService executorService;

	/**
	 * datasource ping开关
	 */
	private String DATASOURCE_PING_OPEN;

	{
		DATASOURCE_PING_OPEN = PropertiesUtil.getProperty(DATASOURCE_PING_OPEN_PROPERTIES);
		if (CommonUtil.isEmpty(DATASOURCE_PING_OPEN))
			DATASOURCE_PING_OPEN = ConstantUtil.FLAG_NO;
	}

	public static DataSourceGroup getDataSourceGroup(String groupName) {

		return groups.get(groupName);
	}

	public static Map<String, DataSourceGroup> getGroups() {

		return groups;
	}

	/**
	 * 配置文件资源注入
	 * 
	 * @param groups
	 */
	@Resource
	public void setGroups(Map<String, DataSourceGroup> groups) {

		DataSourceRouter.groups = groups;
	}

	/**
	 * 对各数据源进行ping故障检测
	 */
	public void init() {

		if (groups == null || groups.isEmpty())
			return;

		for (DataSourceGroup group : groups.values()) {

			if (group != null && group.getShards() != null && !group.getShards().isEmpty()) {

				for (DataSourceShard shard : group.getShards().values()) {

					if (shard != null) {

						if (shard.getMaster() != null)
							dataSourceWrapperList.add(shard.getMaster());
						if (shard.getSlaves() != null && !shard.getSlaves().isEmpty())
							dataSourceWrapperList.addAll(shard.getSlaves());
					}
				}
			}
		}

		if (ConstantUtil.FLAG_YES.equals(DATASOURCE_PING_OPEN)) {

			executorService = Executors.newScheduledThreadPool(dataSourceWrapperList.size());
			for (int i = 0; i < dataSourceWrapperList.size(); i++) {
				executorService.scheduleAtFixedRate(new DataSourcePingThread(dataSourceWrapperList.get(i)), (i + 1) * 2,
						30, TimeUnit.SECONDS);
			}
		}

	}

	/**
	 * 在开发环境中，工程文件有更新时tomcat服务器自动部署并重启，这时Spring容器会重新初始化，
	 * 上次创建的线程池任务仍在运行并使用已经被销毁的数据源而出现错误
	 */
	public void destroy() {

		if (executorService != null && !executorService.isShutdown())
			executorService.shutdownNow();
	}

	/**
	 * 获取指定DataSourceShard的DataSourceWrapper
	 */
	public static DataSourceWrapper getDataSourceWrapperByShardName(String shardName) {

		return getDataSourceWrapperFromShard(getDataSourceShardByGroupAndShardName(shardName), isWriteable());
	}

	/**
	 * 获取可读或可写标识
	 */
	public static boolean isWriteable() {

		/* 从线程变量获取当前GroupName，根据GroupName从容器中获取DataSourceGroup */
		ThreadDataSource threadDataSource = DALMethodInterceptor.getThreadDataSource();
		if (threadDataSource == null)
			return true;

		return threadDataSource.isWriteable();
	}

	/**
	 * 根据GroupName和ShardName获取
	 */
	public static DataSourceShard getDataSourceShardByGroupAndShardName(String shardName) {

		/* 从线程变量获取当前GroupName，根据GroupName从容器中获取DataSourceGroup */
		ThreadDataSource threadDataSource = DALMethodInterceptor.getThreadDataSource();
		if (threadDataSource == null)
			return null;

		DataSourceGroup dataSourceGroup = getDataSourceGroup(threadDataSource.getGroupName());
		if (dataSourceGroup == null)
			return null;

		DataSourceShard shard = null;

		if (shardName == null)
			shard = dataSourceGroup.getMainDataSourceShard();
		else
			shard = dataSourceGroup.getDataSourceShardByName(shardName);

		return shard;
	}

	/**
	 * 从DataSourceShard代表的一组主从数据源中根据读写分离和负载均衡规则获取一个实际的数据源
	 * 
	 * @param dataSourceShard
	 * @return
	 */
	public static DataSourceWrapper getDataSourceWrapperFromShard(DataSourceShard dataSourceShard, boolean writeable) {

		if (dataSourceShard == null)
			return null;

		if (writeable)
			return dataSourceShard.getMaster();
		else
			return getSlaveDataSourceOrder(dataSourceShard);
	}

	private static void slaveDataSourceWeight(DataSourceWrapper wrapper, int index, DataSourceShard dataSourceShard,
			byte strategy) {

		if (strategy == STRATEGY_WEIGHT && wrapper.getWeight() != 1) {
			if (wrapper.counter.get() == wrapper.getWeight()) {
				wrapper.counter.set(1);
			} else {
				wrapper.counter.getAndIncrement();
				index--;
			}
		}

		dataSourceShard.setIndex(index);
		logger.trace(counter.addAndGet(1) + " DataSource name==" + wrapper.getDataSourceName());
	}

	/**
	 * 读库根据轮询和权重自动负载均衡获取一个实际的数据源
	 */
	public static DataSourceWrapper getSlaveDataSourceOrder(DataSourceShard dataSourceShard) {

		if (dataSourceShard == null)
			return null;

		List<DataSourceWrapper> list = dataSourceShard.getSlaves();

		if (list == null || list.isEmpty())
			return dataSourceShard.getMaster();

		DataSourceWrapper wrapper = null;

		int index = dataSourceShard.getIndex();

		lock.lock();
		try {
			for (int i = index + 1; i < list.size(); i++) {
				wrapper = list.get(i);
				if (wrapper.isAvailable()) {
					slaveDataSourceWeight(wrapper, i, dataSourceShard, strategy);
					return wrapper;
				}
			}
			for (int i = 0; i <= index; i++) {
				wrapper = list.get(i);
				if (wrapper.isAvailable()) {
					slaveDataSourceWeight(wrapper, i, dataSourceShard, strategy);
					return wrapper;
				}
			}
			wrapper = dataSourceShard.getMaster();
			dataSourceShard.setIndex(-1);
			logger.debug(counter.addAndGet(1) + " DataSource name==" + wrapper.getDataSourceName());
		} finally {
			lock.unlock();
		}
		return wrapper;
	}

	/**
	 * 数据库健康检测线程
	 * 
	 * @author wuxiaohua
	 * @since 2015-12-30
	 */
	private static class DataSourcePingThread implements Runnable {

		private DataSourceWrapper wrapper;

		public DataSourcePingThread(DataSourceWrapper wrapper) {

			this.wrapper = wrapper;
		}

		@Override
		public void run() {

			dataSourcePing(wrapper);
		}
	}

	/**
	 * 数据库健康检测
	 * 
	 * @param wrapper
	 *            数据源
	 */
	private static void dataSourcePing(DataSourceWrapper wrapper) {

		try {
			wrapper.getJdbcTemplate().execute("select 1");
			wrapper.setAvailable(true);
		} catch (DataAccessException e) {
			wrapper.setAvailable(false);
			e.printStackTrace();
		} finally {

			logger.trace(Thread.currentThread().getName() + " ping dataSource " + wrapper.getDataSourceName());

		}
	}

}