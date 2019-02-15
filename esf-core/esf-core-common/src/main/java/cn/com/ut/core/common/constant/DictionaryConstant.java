package cn.com.ut.core.common.constant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * 字典数据常量
 * 
 * @author wuxiaohua
 * @since 2015-6-17
 * @update 2015-6-17
 */
public class DictionaryConstant {

	/**
	 * Http Header: User-Agent
	 */
	public interface UserAgent {

		String MSIE = "msie";
		String OPERA = "opera";
		String SAFARI = "safari";
		String APPLEWEBKIT = "applewebkit";
		String MOZILLA = "mozilla";
		String CHROME = "chrome";
	}

	/**
	 * 用户历史地址类别
	 */
	public interface UserHistoryAddrClass {

		String POINT = "POINT";
		String AREA = "AREA";
	}

	protected static void reflectField(Class<?> clazz, Map<String, String> map) throws Exception {

		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			if (f.getType().equals(String.class) && Modifier.isPublic(f.getModifiers())
					&& Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())
					&& !f.getName().endsWith("_TXT")) {
				map.put((String) f.get(null),
						(String) clazz.getDeclaredField(f.getName() + "_TXT").get(null));
			}
		}
	}

	/**
	 * Properties属性文件配置常用键值
	 */
	public interface PropertiesKey {
		/**
		 * 系统任务名
		 */
		String TASK_RUN_SYNC = "task.run.sync";
		/**
		 * elasticsearch集群名
		 */
		String ES_CLUSTER_NAME = "es.cluster.name";
		/**
		 * 推送服务器host
		 */
		String PUSHSRV_HOST = "pushsrv.host";
		/**
		 * 推送服务器端口
		 */
		String PUSHSRV_PORT = "pushsrv.port";
		/**
		 * 推送服务器开关
		 */
		String PUSHSRV_OPEN = "pushsrv.open";
		/**
		 * mongodb服务器开关
		 */
		String MONGODB_CLUSTER_OPEN = "mongodb.cluster.open";
		/**
		 * ElasticSearch服务器开关
		 */
		String ES_CLUSTER_OPEN = "es.cluster.open";
		/**
		 * datasource ping开关
		 */
		String DATASOURCE_PING_OPEN = "datasource.ping.open";
		/**
		 * 数据库方言
		 */
		String DATABASE_DIALECT = "database.dialect";
	}

	/**
	 * 任务类型
	 */
	public interface TaskStatus {

		/**
		 * 任务未分配
		 */
		String NEW = "NEW";
		/**
		 * 任务已临时分配
		 */
		String TEMP = "TEMP";
		/**
		 * 任务锁定
		 */
		String LOCKED = "LOCKED";
		/**
		 * 任务挂起
		 */
		String HANDUP = "HANDUP";
	}

	/**
	 * 任务类型
	 */
	public interface TaskType {

		/**
		 * 审核AUDIT
		 */
		String AUDIT = "AUDIT";
	}

	/**
	 * 角色类别
	 */
	public interface RoleClass {

		/**
		 * 系统预定义角色
		 */
		String SYSTEM_ROLE = "0";
		/**
		 * 企业自定义角色
		 */
		String CORP_ROLE = "1";
	}

	/**
	 * 资质审核类型
	 */
	public static interface AuthType {

		/**
		 * 实名认证（身份证）
		 */
		public static final String REALNAME = "REALNAME";

	}

	/**
	 * 短消息类型 MESSAGE_SHORT_TYPE
	 */
	public interface MESSAGE_SHORT_TYPE {
		public static final String PRIVATE = "0";
		public static final String PUBLIC = "1";
		public static final String SYSTEM = "2";
	}

	/**
	 * 消息发送类型，字典项MESSAGE_SEND_TYPE
	 */
	public interface MESSAGE_SEND_TYPE {
		public static final String PERSON = "0";
		public static final String GROUP = "1";
		public static final String ALL = "2";
	}

	/**
	 * 消息分类，字典项MESSAGE_CATEGORY
	 */

	public interface MESSAGE_CATEGORY {
		/**
		 * 短消息
		 */
		public static final String SHORT = "0";
		/**
		 * 通知
		 */
		public static final String NOTICE = "1";
	}

}
