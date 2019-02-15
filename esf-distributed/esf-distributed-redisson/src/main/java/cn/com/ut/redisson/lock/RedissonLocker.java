package cn.com.ut.redisson.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author wuxiaohua
 * @since 2018/10/30
 */
@Component
public class RedissonLocker {

	@Autowired
	private RedissonClient redissonClient;

	private Random random = new Random(100);

	public LockHold tryLock(String lockName, Long waitTime, Long leaseTime, boolean randomDelay,
			LockEnum lockEnum) {

		Assert.notNull(lockName, "lockName can't be empty");

		if (randomDelay) {
			try {
				// 将被赋值为一个 MIN 和 MAX 范围内的随机数
				// random.nextInt(MAX - MIN + 1) + MIN;
				TimeUnit.MILLISECONDS.sleep(random.nextInt(30 - 10 + 1) + 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		boolean locked = false;
		RLock rLock = null;
		RReadWriteLock rReadWriteLock = null;
		LockHold lockHold = new LockHold();
		switch (lockEnum) {
		case FairLock:
			rLock = redissonClient.getFairLock(lockName);
			break;
		case Lock:
			rLock = redissonClient.getLock(lockName);
			break;
		case ReadLock:
			rLock = redissonClient.getReadWriteLock(lockName).readLock();
			break;
		case WriteLock:
			rLock = redissonClient.getReadWriteLock(lockName).writeLock();
			break;
		default:
			break;
		}

		lockHold.setRLock(rLock);
		if (waitTime != null && leaseTime != null) {
			try {
				locked = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (waitTime != null) {
			try {
				locked = rLock.tryLock(waitTime, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			locked = rLock.tryLock();
		}
		lockHold.setLocked(locked);
		return lockHold;
	}

	public LockHold tryLock(String lockName, Long waitTime, Long leaseTime, LockEnum lockEnum) {

		return this.tryLock(lockName, waitTime, leaseTime, false, lockEnum);
	}

	public LockHold tryLock(String lockName, Long waitTime, LockEnum lockEnum) {

		return this.tryLock(lockName, waitTime, null, false, lockEnum);
	}

	public LockHold tryLock(String lockName, LockEnum lockEnum) {

		return this.tryLock(lockName, null, null, false, lockEnum);
	}

}
