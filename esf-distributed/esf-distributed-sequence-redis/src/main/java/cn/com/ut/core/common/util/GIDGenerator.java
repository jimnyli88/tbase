package cn.com.ut.core.common.util;

import java.util.ArrayList;
import java.util.List;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * twitter的snowflake算法，全局唯一ID生成
 * 
 * @author wuxiaohua
 * @since 2018年10月23日
 */
@Configuration
@ConfigurationProperties(prefix = "gid")
@Slf4j
@NoArgsConstructor
public class GIDGenerator implements InitializingBean {

	// @Autowired(required = false)
	// private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private RedissonClient redissonClient;
	private static GIDGenerator instance;

	public static GIDGenerator getInstance() {

		return GIDGenerator.instance;
	}

	/**
	 * 起始的时间戳
	 */
	@Getter
	@Setter
	private long timestamp = 1490233606426L;
	/**
	 * 数据中心标识
	 */
	@Getter
	@Setter
	private long datacenter = 0L;
	private boolean init = false;

	/**
	 * 订单前缀
	 */
	private static final String PREFIX_ORDER = "1";

	/**
	 * 交易流水号前缀
	 */
	private static final String PREFIX_TRADE = "9";

	/**
	 * 退款流水号前缀
	 */
	private static final String PREFIX_RETURN = "8";
	/**
	 * 缓存key
	 */
	private static final String GID_KEY = "_gid_sequence_";

	/**
	 * 每一部分占用的位数
	 */
	private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
	private final static long DATACENTER_BIT = 2;// 数据中心占用的位数
	private final static long WORKER_BIT = 8; // 机器标识占用的位数

	/**
	 * 每一部分的最大值
	 */
	private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
	private final static long MAX_WORKER_NUM = -1L ^ (-1L << WORKER_BIT);
	private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

	/**
	 * 每一部分向左的位移
	 */
	private final static long WORKER_LEFT = SEQUENCE_BIT;
	private final static long DATACENTER_LEFT = SEQUENCE_BIT + WORKER_BIT;
	private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

	private long workerId = -1L; // 机器标识
	private long sequence = 0L; // 序列号
	private long lastStmp = -1L;// 上一次时间戳

	@Override
	public void afterPropertiesSet() throws Exception {

		if (init)
			return;

		init = true;
		GIDGenerator.instance = this;

		if (datacenter > MAX_DATACENTER_NUM || datacenter < 0) {
			throw new IllegalArgumentException(
					"datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		}

		getWorkerId();

		if (workerId > MAX_WORKER_NUM || workerId < 0) {
			throw new IllegalArgumentException(
					"workerId can't be greater than MAX_WORKER_NUM or less than 0");
		}

	}

	/**
	 * 生成订单编号
	 */
	public String genOrderSequence() {

		return genSequence(PREFIX_ORDER);
	}

	/**
	 * 生成支付流水号
	 */
	public String genTradeSequence() {

		return genSequence(PREFIX_TRADE);
	}

	/**
	 * 生成退款流水号
	 */
	public String genReturnSequence() {

		return genSequence(PREFIX_RETURN);
	}

	/**
	 * 生成全局唯一ID
	 * 
	 * @param prefix
	 *            前缀
	 * @param number
	 *            数量
	 * @return
	 */
	public List<String> genSequences(String prefix, int number) {

		List<String> sequences = new ArrayList<String>();
		if (number < 1)
			number = 1;
		if (prefix == null)
			prefix = "";
		for (int i = 0; i < number; i++) {
			sequences.add(prefix + nextId());
		}
		return sequences;
	}

	/**
	 * 生成全局唯一ID
	 * 
	 * @param prefix
	 *            前缀
	 * @return
	 */
	public String genSequence(String prefix) {

		return genSequences(prefix, 1).get(0);
	}

	/**
	 * 产生下一个ID
	 *
	 * @return
	 */
	public synchronized long nextId() {

		long currStmp = getCurrStmp();
		if (currStmp < lastStmp) {
			throw new RuntimeException("Clock moved backwards. Refusing to generate id");
		}

		if (currStmp == lastStmp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & MAX_SEQUENCE;
			// 同一毫秒的序列数已经达到最大
			if (sequence == 0L) {
				currStmp = getNextMill();
			}
		} else {
			// 不同毫秒内，序列号置为0
			sequence = 0L;
		}

		lastStmp = currStmp;

		return (currStmp - timestamp) << TIMESTMP_LEFT // 时间戳部分
				| datacenter << DATACENTER_LEFT // 数据中心部分
				| getWorkerId() << WORKER_LEFT // 机器标识部分
				| sequence; // 序列号部分
	}

	/**
	 * 获取最新毫秒时间戳
	 * 
	 * @return
	 */
	private long getNextMill() {

		long mill = getCurrStmp();
		while (mill <= lastStmp) {
			mill = getCurrStmp();
		}
		return mill;
	}

	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	private long getCurrStmp() {

		return System.currentTimeMillis();
	}

	/**
	 * 获取本机的工作者ID
	 * 
	 * @return
	 */
	private long getWorkerId() {

		if (this.workerId == -1L) {
			synchronized (this) {
				if (this.workerId == -1L) {
					try {
						// this.workerId = redisTemplate.opsForValue().increment(GID_KEY +
						// datacenter,
						// 1);
						this.workerId = redissonClient.getAtomicLong(GID_KEY + datacenter)
								.getAndAdd(1);

						if (this.workerId > MAX_WORKER_NUM) {
							this.workerId = 0L;
							// redisTemplate.opsForValue().set(GID_KEY + datacenter, this.workerId);
							redissonClient.getAtomicLong(GID_KEY + datacenter)
									.getAndSet(this.workerId);
						}
						log.info("==getWorkerId finish==" + workerId);
					} catch (Exception ex) {
						this.workerId = 0L;
						log.error("==getWorkerId fail==" + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		}

		return this.workerId;
	}

	/**
	 * 重置workId
	 */
	@Scheduled(initialDelay = 30 * 1000, fixedRate = 7 * 24 * 3600 * 1000)
	public void resetWorkId() {

		this.workerId = -1L;
	}

}
