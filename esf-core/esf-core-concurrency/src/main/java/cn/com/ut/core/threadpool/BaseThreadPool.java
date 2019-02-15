package cn.com.ut.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.ut.core.common.constant.ConstantUtil;
/**
* 线程池
* @author wuxiaohua
* @since 2013-12-22下午2:27:50
*/
public class BaseThreadPool {

	/**
	 * executorService
	 */
	private static ExecutorService executorService;

	/**
	 * get ExecutorService
	 * @return ExecutorService
	 */
	private static ExecutorService getExecutorService() {

		if (!isAvailable()) {
			synchronized (BaseThreadPool.class) {
				if (!isAvailable())
					executorService = Executors.newCachedThreadPool();
			}
		}
		return executorService;
	}

	/**
	 * 执行任务
	 * @param task 
	 */
	public static void execute(Runnable task) {

		getExecutorService().execute(task);
	}

	/**
	 * 
	 * @return 是否可用
	 */
	private static boolean isAvailable() {

		if (executorService == null || executorService.isShutdown()
				|| executorService.isTerminated())
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param worker
	 * @param invokeSync 
	 */
	public static void doWork(final Worker worker, String invokeSync) {

		if (invokeSync.equals(ConstantUtil.FLAG_YES)) {
			worker.doWork();
		} else {
			execute(new Runnable() {

				@Override
				public void run() {

					worker.doWork();
				}
			});
		}
	}
}
