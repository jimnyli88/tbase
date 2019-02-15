package cn.com.ut.redisson.lock;

import org.redisson.api.RLock;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wuxiaohua
 * @since 2018/10/30
 */
@Setter
@Getter
public class LockHold {

	private boolean locked = false;
	private RLock rLock;

	public void unlock() {

		rLock.unlock();
	}
}
