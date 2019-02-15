package cn.com.ut.core.common.constant;

import java.io.InputStream;

import cn.com.ut.core.common.util.io.FileUtil;

/**
 * 常量工具类
 * 
 * @author wuxiaohua
 * @since 2013-10-10
 * @update 2015-12-4
 *
 */
public class ConstantUtil {

	/**
	 * 业务错误码
	 */
	public static final int BIZ_ERR_CODE = -1;
	/**
	 * 异常错误码
	 */
	public static final int UNKNOWN_ERR_CODE = -2;
	/**
	 * 正常返回码
	 */
	public static final int SUCCESS_CODE = 0;

	/**
	 * 全文检索类型类别
	 */
	public interface INDEX_TYPE {

		/**
		 * 创建
		 */
		String CREATE = "CREATE";
		/**
		 * 跟新
		 */
		String UPDATE = "UPDATE";
		/**
		 * 不存在则创建
		 */
		String NOTEXITCREATE = "NOTEXITCREATE";
	}

	/**
	 * 业务逻辑常用提示或退出消息
	 * 
	 * @author wuxiaohua
	 * @since 2016年4月29日
	 */
	public interface BizMsg {

		String NOT_EXIST = "记录不存在";
		String CANNOT_MODIFY = "记录不能被修改";
		String CANNOT_DELETE = "记录不能被删除";
	}

	/**
	 * 保存默认图片的byte数组
	 */
	public static byte[] IMG_DEFAULT;

	/**
	 * 默认图片存放路径
	 */
	public static final String IMG_DEFAULT_PATH = "cn/com/ut/resource/image/nofound.jpg";

	/**
	 * 初始化默认图片数组
	 */
	static {

		InputStream in = FileUtil.inputStreamReader(IMG_DEFAULT_PATH);
		IMG_DEFAULT = FileUtil.inputStreamReader(in);
	}

	public static final String DEFAULT_DOMAIN = "ut.com";

	/**
	 * 网络地址
	 */
	public static final String LOOPBACK = "127.0.0.1";
	/**
	 * localhost
	 */
	public static final String LOCALHOST = "localhost";
	/**
	 * http://127.0.0.1:8888/
	 */
	public static final String HESSIAN_REMOTE_HOST = "http://127.0.0.1:8888/";

	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final String DATETIME_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
	/**
	 * HH:mm:ss
	 */
	public static final String TIME_FORMAT = "HH:mm:ss";
	/**
	 * HH:mm:ss,SSS
	 */
	public static final String TIME_FULL_FORMAT = "HH:mm:ss,SSS";
	/**
	 * yyyyMMddHHmmss
	 */
	public static final String DATETIME_SHORT_FORMAT = "yyyyMMddHHmmss";
	/**
	 * yyyyMMddHHmmssSSS
	 */
	public static final String DATETIME_SHORT_FULL_FORMAT = "yyyyMMddHHmmssSSS";
	/**
	 * yyyyMMdd
	 */
	public static final String DATE_SHORT_FORMAT = "yyyyMMdd";

	/**
	 * yyMMdd
	 */
	public static final String DATE_SHORT_FORMAT_2 = "yyMMdd";
	/**
	 * HHmmss
	 */
	public static final String TIME_SHORT_FORMAT = "HHmmss";
	/**
	 * HHmmssSSS
	 */
	public static final String TIME_SHORT_FULL_FORMAT = "HHmmssSSS";

	/**
	 * 执行结果状态码,表示有错误,-1
	 */
	public static final String ERR_CODE = "-1";
	/**
	 * 执行结果状态码,表示有正确,0
	 */
	public static final String SUC_CODE = "0";
	/**
	 * fail
	 */
	public static final String ERR_MSG = "fail";
	/**
	 * success
	 */
	public static final String SUC_MSG = "success";

	/**
	 * SUCCESS
	 */
	public static final String SUCCESS = "SUCCESS";

	/**
	 * FAIL
	 */
	public static final String FAIL = "FAIL";

	/**
	 * 操作成功
	 */
	public static final String SUC_MSG_CN = "操作成功!";
	/**
	 * 操作失败
	 */
	public static final String ERR_MSG_CN = "操作失败!";
	/**
	 * 请求参数错误
	 */
	public static final String REQ_ERR_MSG = "请求参数错误!";
	/**
	 * 填项不能为空
	 */
	public static final String REQUIRED_MISS = "必填项不能为空!";
	/**
	 * 会话失效，请重新登录
	 */
	public static final String ERR_SESSION_EXPIRE = "会话失效，请重新登录!";
	/**
	 * 指定参数的数据不可重复
	 */
	public static final String ERR_PARAM_REPEAT = "指定参数的数据不可重复";
	/**
	 * 未登陆或会话过期
	 */
	public static final String RC_SESSION_EXPIRE = "-7";
	/**
	 * 会话有效但未授予接口访问权限
	 */
	public static final String RC_NO_PERMISSION = "-8";

	/**
	 * 其他标识:Y
	 */
	public static final String FLAG_YES = "Y";
	/**
	 * 其他标识:N
	 */
	public static final String FLAG_NO = "N";
	/**
	 * 1
	 */
	public static final String FLAG_ONE = "1";
	/**
	 * 0
	 */
	public static final String FLAG_ZERO = "0";

	/**
	 * true
	 */
	public static final String FLAG_TRUE = "true";
	/**
	 * false
	 */
	public static final String FLAG_FALSE = "false";

	/**
	 * null
	 */
	public static final String STR_NULL = "null";
	/**
	 * 空串
	 */
	public static final String STR_EMPTY = "";
	/**
	 * 空格
	 */
	public static final String STR_SPACE = " ";
	/**
	 * 换行
	 */
	public static final String STR_ENTER = "\n";

	/**
	 * 缓存类型:memcached
	 */
	public static final String CACHE_TYPE_MEMCACHED = "memcached";
	/**
	 * 缓存类型:redis
	 */
	public static final String CACHE_TYPE_REDIS = "redis";
	/**
	 * 缓存类型:ehcache
	 */
	public static final String CACHE_TYPE_EHCACHE = "ehcache";

	/**
	 * 缓存级别
	 */
	public static final String CACHE_LEVEL_1 = "L1";
	/**
	 * 缓存级别
	 */
	public static final String CACHE_LEVEL_2 = "L2";

	/**
	 * 缓存存储范围:本地缓存
	 */
	public static final String CACHE_STORE_LOCAL = "LS";
	/**
	 * 缓存存储范围：远程缓存
	 */
	public static final String CACHE_STORE_REMOTE = "RS";

	/**
	 * 分布式文件系统类型:Hadoop
	 */
	public static final String DFS_TYPE_HADOOP = "Hadoop";
	/**
	 * 分布式文件系统类型:GridFS
	 */
	public static final String DFS_TYPE_GRIDFS = "GridFS";

	/**
	 * 系统用户标识
	 */
	public static final String SYS_USER = "sys";

	/**
	 * 随机字符串-仅限数字和字母; 排除数字0、1和9; 排除小写字母o,l,q; 排除大写字母I,O
	 */
	public static final String RANDOM_CHAR = "2345678abcdefghijkmnprstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
	/**
	 * 随机字符串-仅限数字0到9
	 */
	public static final String RANDOM_NUMBER = "0123456789";

	/**
	 * 字符集 UTF-8
	 */
	public static final String CHARSET_UTF8 = "UTF-8";
	/**
	 * 字符集 ISO-8859-1
	 */
	public static final String CHARSET_ISO = "ISO-8859-1";
	/**
	 * 字符集 GBK
	 */
	public static final String CHARSET_GBK = "GBK";
	/**
	 * 字符集 US-ASCII
	 */
	public static final String CHARSET_ASCII = "US-ASCII";

	/**
	 * MIME TYPE image/jpeg
	 */
	public static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
	/**
	 * MIME TYPE application/x-msdownload
	 */
	public static final String MIME_TYPE_MSDOWNLOAD = "application/x-msdownload";

	/**
	 * CONTENT TYPE text/xml; charset=UTF-8
	 */
	public static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";
	/**
	 * CONTENT TYPE application/json; charset=UTF-8
	 */
	public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
	/**
	 * CONTENT TYPE text/html; charset=UTF-8
	 */
	public static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";
	/**
	 * CONTENT TYPE text/plain; charset=UTF-8
	 */
	public static final String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";
	/**
	 * CONTENT TYPE multipart/form-data; charset=UTF-8
	 */
	public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; charset=UTF-8";

	/**
	 * JSON对象为空
	 */
	public static final String JSON_OBJECT_EMPTY = "{}";
	/**
	 * JSON Array为空
	 */
	public static final String JSON_ARRAY_EMPTY = "[]";

	/**
	 * 请求数据格式:JSON
	 */
	public static final byte REQ_DATA_JSON = 0x01;
	/**
	 * 请求数据格式:XML
	 */
	public static final byte REQ_DATA_XML = 0x02;
	/**
	 * 请求数据格式:OTHER
	 */
	public static final byte REQ_DATA_OTHER = 0x03;
	/**
	 * 请求数据格式:BINARY
	 */
	public static final byte REQ_DATA_BINARY = 0x04;
	/**
	 * 请求数据格式:FORM
	 */
	public static final byte REQ_DATA_FORM = 0x05;

	/**
	 * XML结点名称 req
	 */
	public static final String XML_REQ = "req";
	/**
	 * XML结点名称 res
	 */
	public static final String XML_RES = "res";
	/**
	 * XML结点名称 cont
	 */
	public static final String XML_CONT = "cont";
	/**
	 * XML结点名称 row
	 */
	public static final String XML_ROW = "row";
	/**
	 * XML结点名称 rownum
	 */
	public static final String XML_ROWNUM = "rownum";
	/**
	 * XML结点名称 call
	 */
	public static final String XML_CALL = "call";
	/**
	 * XML结点名称
	 */
	public static final String XML_LIB = "lib";
	/**
	 * XML结点名称 func
	 */
	public static final String XML_FUNC = "func";
	/**
	 * XML结点名称 par
	 */
	public static final String XML_PAR = "par";
	/**
	 * XML结点名称 where
	 */
	public static final String XML_PAR_WHERE = "where";
	/**
	 * XML结点名称 orderby
	 */
	public static final String XML_PAR_ORDERBY = "orderby";
	/**
	 * XML结点名称 field
	 */
	public static final String XML_FIELD = "field";
	/**
	 * XML结点名称 name
	 */
	public static final String XML_FIELD_NAME = "name";
	/**
	 * XML结点名称 id
	 */
	public static final String XML_FIELD_ID = "id";
	/**
	 * XML结点名称 type
	 */
	public static final String XML_FIELD_TYPE = "type";
	/**
	 * XML结点名称 value
	 */
	public static final String XML_FIELD_VALUE = "value";
	/**
	 * XML结点名称 op
	 */
	public static final String XML_FIELD_OP = "op";
	/**
	 * XML结点名称 op
	 */
	public static final String XML_FIELD_NULLCASE = "nullcase";
	/**
	 * XML结点名称 rc
	 */
	public static final String XML_RC = "rc";
	/**
	 * XML结点名称 err
	 */
	public static final String XML_ERR = "err";
	/**
	 * XML结点名称 /req/par
	 */
	public static final String PARENT_ELEMENT = "/req/par";
	/**
	 * XML结点名称 _TEXT
	 */
	public static final String DICTIONARY_POSTFIX_UPPER = "_TEXT";
	/**
	 * XML结点名称 _text
	 */
	public static final String DICTIONARY_POSTFIX_LOWER = "_text";
	/**
	 * XML结点名称 menu
	 */
	public static final String XML_TREE_NODE = "menu";

	/**
	 * 分页头信息字段 page
	 */
	public static final String PAGE = "page";
	/**
	 * 分页头信息字段 pageno
	 */
	public static final String PAGE_NO = "pageno";
	/**
	 * 分页头信息字段 pagesize
	 */
	public static final String PAGE_SIZE = "pagesize";
	/**
	 * 分页头信息字段 records
	 */
	public static final String RECORDS = "records";
	/**
	 * 分页头信息字段 total
	 */
	public static final String PAGE_TOTAL = "total";
	/**
	 * 分页头信息字段 pagegroup
	 */
	public static final String PAGE_GROUP = "pagegroup";

	/**
	 * 用户类型定义:普通用户
	 */
	public static final String USER_TYPE_PERSON = "P";
	/**
	 * 用户类型定义:企业用户
	 */
	public static final String USER_TYPE_ENTERPRISE = "E";
	/**
	 * 用户类型定义:管理用户
	 */
	public static final String USER_TYPE_ADMIN = "A";
	/**
	 * 用户类型定义:企业子账户
	 */
	public static final String USER_TYPE_SUB = "US";
	/**
	 * 用户类型定义:客服
	 */
	public static final String USER_TYPE_CUSTOMSERVICE = "C";

	/**
	 * 登录类型:浏览器
	 */
	public static final String USER_LOGON_TYPE_IE = "B";
	/**
	 * 登录类型:APP
	 */
	public static final String USER_LOGON_TYPE_APP = "APP";

	/**
	 * 根据用户类型，用户默认所属的角色，系统须对用户进行默认权限分配
	 */
	public static final String ROLE_PERSON = "0cbf0165cea5413a811513bedd211119";

	/**
	 * 游客权限
	 */
	public static final String ROLE_NOTLOGON = "67389ff1a438475eb8839d10ea0735a5";

	/**
	 * 管理员权限
	 */
	public static final String ROLE_ADMIN = "0926e968eb7c43ffb6a7ae94ed5c5106";

	/**
	 * 逻辑关系或
	 */
	public static final String LOGIC_OR = "OR";
	/**
	 * 逻辑关系与
	 */
	public static final String LOGIC_AND = "AND";

	/**
	 * 超时设置，分钟
	 */
	public static final int RSA_TIMEOUT_MINUTES = 0;

	/**
	 * 会话标识
	 */
	public static final String SESSIONKEY = "sessionKey";
	/**
	 * 会话标识
	 */
	public static final String ITEMCARTID = "itemCartId";
	/**
	 * 用户坐标地址会话标识
	 */
	public static final String POSITION = "position";
	/**
	 * 用户城市区域地址会话标识
	 */
	public static final String AREA = "area";
	/**
	 * 会话标识
	 */
	public static final String DOMAIN = "domain";

	/**
	 * 表达式及运算符
	 */
	public static interface Expression {
		String AND = "AND";
		String OR = "OR";
		char AND_SIGN = '*';
		char OR_SIGN = '#';
		char LEFT_BRACKET = '(';
		char RIGHT_BRACKET = ')';
	}

	/**
	 * 账号类型 0：用户名
	 */
	public static final int ACCOUNT_TYPE_USERNAME = 0;
	/**
	 * 账号类型 1：邮箱
	 */
	public static final int ACCOUNT_TYPE_EMAIL = 1;
	/**
	 * 账号类型 2：手机
	 */
	public static final int ACCOUNT_TYPE_MOBILE = 2;
	/**
	 * 账号类型 -1：其他
	 */
	public static final int ACCOUNT_TYPE_OTHER = -1;

	/**
	 * 默认账户密码
	 */
	public static final String DEFAULT_ACCOUNT_PWD = "123456";

	/**
	 * 系統开始时间
	 */
	public static final String SYSTEM_START_TIME = "2015-01-01 00:00:00";

	/**
	 * 审核状态
	 */
	public static interface AuditStatus {

		/**
		 * 未提交审核
		 */
		public static final String AUDIT_NONE = "-1";
		/**
		 * 准备审核
		 */
		public static final String AUDIT_READY = "0";
		/**
		 * 审核中
		 */
		public static final String AUDIT_BEGIN = "1";
		/**
		 * 审核通过
		 */
		public static final String AUDIT_PASS = "2";
		/**
		 * 审核未通过
		 */
		public static final String AUDIT_REJECT = "3";

	}

	/**
	 * 资质认证动态表单字段类型
	 */
	public static interface AuthFieldFormat {

		/**
		 * 文本
		 */
		public static final String TXT = "TXT";
		/**
		 * 附件（单文件）
		 */
		public static final String ONE_RES = "ONE_RES";
		/**
		 * 附件（多文件）
		 */
		public static final String MANY_RES = "MANY_RES";
	}

	/**
	 * 支付状态
	 */
	public interface PAY_STATUS {
		/**
		 * 未付款
		 */
		public static final int nopay = 0;
		/**
		 * 付款中
		 */
		public static final int paying = 1;
		/**
		 * 已付款
		 */
		public static final int payed = 2;
	}

	/**
	 * 用户资源空间：标签开放权限
	 * 
	 */
	public interface label_authority {
		/**
		 * 紧自己可见
		 */
		public static final String openToMine = "0";
		/**
		 * 对好友可见
		 */
		public static final String openToFriends = "1";
		/**
		 * 对所有人可见
		 */
		public static final String openAll = "2";
	}

	/**
	 * 是点坐标的类型
	 * 
	 */
	public interface PointType {
		/**
		 * 企业地址
		 */
		public static final String GEO_EP = "0";
		/**
		 * 店铺地址
		 */
		public static final String GEO_SHOP = "1";
		/**
		 * 仓库地址
		 */
		public static final String GEO_SHOPSTORGE = "2";
		/**
		 * 用户家庭地址
		 */
		public static final String GEO_PHOMEADDR = "3";
		/**
		 * 用户收货地址
		 */
		public static final String GEO_RECEIVEADDR = "4";
		/**
		 * 用户曾用历史地址
		 */
		public static final String GEO_USER_HISTORY_ADDR = "5";
	}

	/**
	 * 是面坐标的类型
	 * 
	 */
	public interface PolygonType {
		/**
		 * 店铺运费模板运费方式对应的配送范围坐标地址
		 */
		public static final String GEO_SFEE_RANGE = "0";
		/**
		 * 店铺仓库配送范围坐标地址
		 */
		public static final String GEO_SHOPSTORGE_RANGE = "1";
		/**
		 * 店铺配送范围区域
		 */
		public static final String GEO_SHOP_RANGE = "2";
	}

	/**
	 * 空间几何类型
	 */
	public interface SPACE_TYPE {
		/**
		 * 点
		 */
		public static final String point = "point";
		/**
		 * 线
		 */
		public static final String linestring = "linestring";
		/**
		 * 面
		 */
		public static final String polygon = "polygon";
		/**
		 * 多面
		 */
		public static final String multipolygon = "multipolygon";
	}

	/**
	 * 区域选择类型
	 */
	public interface AREA_SELECT {
		/**
		 * 正向
		 */
		public static final String Forward = "0";
		/**
		 * 反向
		 */
		public static final String Reverse = "1";
	}

	/**
	 * 聊天类型
	 * 
	 */
	public interface RECORD_TYPE {

		/**
		 * "GROUPCHAT":群聊;
		 */

		public static final String GROUPCHAT = "GROUPCHAT";
		/**
		 * "ONECHAT":单聊;
		 */

		public static final String ONECHAT = "ONECHAT";
	}

	/**
	 * 消息接收状态
	 */
	public interface ACCEPT_STATUS {
		/**
		 * 未读
		 */
		public static final String NO_READ = "NO_READ";
		/**
		 * 已读
		 */
		public static final String READED = "READED";
	}

	/**
	 * 用户加入群组的状态
	 */
	public interface JOIN_STATUS {

		/**
		 * "PEND_APPROVAL":待批准;
		 */
		public static final String PEND_APPROVAL = "PEND_APPROVAL";
		/**
		 * "AGREEN":同意;
		 */
		public static final String AGREEN = "AGREEN";
		/**
		 * "REFUSE":拒绝;
		 */
		public static final String REFUSE = "REFUSE";
	}

	/**
	 * 默认的用户分组
	 */
	public interface DEFAULT_GROUP {
		/**
		 * "CONNECT_LATER":最近联系人;
		 */
		// public static final String CONNECT_LATER = "最近联系人";
		/**
		 * "FRIENDS":我的好友;
		 */
		public static final String FRIENDS = "我的好友";
		/**
		 * "STRANGER":陌生人;
		 */
		public static final String STRANGER = "陌生人";
		/**
		 * "BLACK":黑名单;
		 */
		public static final String BLACK = "黑名单";
	}

	/**
	 * 群组验证
	 */
	public interface TEAM_VERIFY {
		/**
		 * "PUBLIC":公开;
		 */
		public static final String PUBLIC = "PUBLIC";
		/**
		 * "VERIFY":身份验证;
		 */
		public static final String VERIFY = "VERIFY";
		/**
		 * "PRIVATE":私密
		 */
		public static final String PRIVATE = "PRIVATE";
	}

	/**
	 * 认证类型
	 */
	public interface AUTH_TYPE {
		/**
		 * 个人用户实名认证
		 */
		public static final String REALNAME = "REALNAME";

	}

	// hadoop用的时候需要切分
	public static int[] pictureWidth = new int[] { 320, 640, 720 };

	/**
	 * 微信支付类型
	 */
	public interface WEIXIN_PAY_TYPE {

		/**
		 * 公众号支付
		 */
		String JSAPI = "WeChat";
		/**
		 * 扫码支付
		 */
		String NATIVE = "PC";
		/**
		 * APP支付
		 */
		String APP = "APP";
	}

	/**
	 * 阿里支付类型
	 */
	public interface ALI_PAY_TYPE {

		/**
		 * PC支付
		 */
		String PC = "PC";
		/**
		 * H5支付
		 */
		String ALIH5 = "WAP";
		/**
		 * APP支付
		 */
		String ALIAPP = "APP";
	}

	/**
	 * 支付类型
	 */
	public interface PAY_TYPE {

		/**
		 * 微信支付
		 */
		String WEIXIN = "WeChatPay";

		/**
		 * 支付宝支付
		 */
		String ZHIFUBAO = "Alipay";

		/**
		 * 开发环境使用
		 */
		String TEST = "Test";
	}

	/**
	 * 支付超时时间
	 */
	public interface PAY_TIME_OUT {
		/**
		 * 创建支付超时时间
		 */
		String CREATE_PAY_TIME_OUT = "CREATE_PAY_TIME_OUT";
	}

	/**
	 * 支付超时时间
	 */
	public interface CACHE_EXPIRE {

		String ONE_DAY = "ONE_DAY";
		String ONE_HOUR = "ONE_HOUR";
		String HALF_HOUR = "HALF_HOUR";
		String TEN_MIN = "TEN_MIN";
	}
	
	/**
	 * 系统标签ID
	 * 
	 * @author wuxiaohua
	 * @since 2016年6月2日
	 */
	public interface LABEL {

		/**
		 * 公共元件标签
		 */
		String PUBLIC_ELEMENT = "139310683a4a45ffa82c5c5678694e5f";
	}
	
}
