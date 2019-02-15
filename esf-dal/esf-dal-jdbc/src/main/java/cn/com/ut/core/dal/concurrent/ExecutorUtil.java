package cn.com.ut.core.dal.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorUtil {

	static class MyThread implements Runnable {

		private final CountDownLatch countDownLatch;

		public MyThread(CountDownLatch countDownLatch) {

			this.countDownLatch = countDownLatch;
		}

		public MyThread() {

			this(null);
		}

		@Override
		public void run() {

			System.out.println(Thread.currentThread().getName());
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			countDownLatch.countDown();
		}
	}

	public static void main(String[] args) throws Exception {

		ExecutorService service = Executors.newCachedThreadPool();

		CountDownLatch countDownLatch = new CountDownLatch(2);
		// service.execute(new MyThread(countDownLatch));
		// service.execute(new MyThread(countDownLatch));

		Callable<Integer> c1 = new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {

				for (int i = 0; i < 10; i++) {
					Thread.sleep(10);
					System.out.println("==2");
				}
				return 2;
			}
		};

		Callable<Integer> c2 = new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {

				for (int i = 0; i < 10; i++) {
					Thread.sleep(10);
					System.out.println("==4");
				}
				return 4;
			}
		};

		// Future f1 = null;
		// Future f2 = null;
		// try {
		//
		// f1 = service.submit(new MyThread(countDownLatch));
		// System.out.println(f1.get(3, TimeUnit.SECONDS));
		// }catch(Exception e) {
		// f1.cancel(true);
		// e.printStackTrace();
		// }
		//
		// try {
		// f2 = service.submit(new MyThread(countDownLatch));
		//
		// System.out.println(f2.get(3, TimeUnit.SECONDS));
		// } catch (Exception e) {
		// f2.cancel(true);
		// e.printStackTrace();
		// }
		//
		// countDownLatch.await();

		Future<Integer> fa = service.submit(c1);
		Future<Integer> fb = service.submit(c2);

		System.out.println(fa.get() + fb.get());

		System.out.println("main==");

	}
}
