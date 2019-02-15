package cn.com.ut.core.environment;

/**
 * 当前操作系统环境
 * 
 * @author wuxiaohua
 *
 */
public class SystemEnvironment {

	private static boolean winOs;
	private static String tmpDir;

	public static boolean isWinOs() {

		return winOs;
	}

	public static String getTmpDir() {

		return tmpDir;
	}

	static {

		tmpDir = System.getProperty("java.io.tmpdir");
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("windows") != -1) {
			winOs = true;
		}
	}

}
