package cn.com.ut.core.common.system.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.net.HostAddress;

/**
 * 系统用户信息实体
 * 
 * @author wuxiaohua
 * @since 2013-12-22下午2:17:45
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9056259423280594860L;

	private SessionKey sessionKey;

	/**
	 * userId
	 */
	private String userId;
	/**
	 * userName
	 */
	private String userName;
	/**
	 * userType
	 */
	private String userType;
	/**
	 * email
	 */
	private String email;
	/**
	 * sessionId
	 */
	private String sessionId;
	/**
	 * randomCode
	 */
	private String randomCode;

	/**
	 * 用户角色列表
	 */
	private List<String> roles;
	/**
	 * 用户登录主机
	 */
	private Set<HostAddress> hosts = new HashSet<HostAddress>();
	/**
	 * interfaces
	 */
	private Set<String> interfaces;
	/**
	 * 用户权限菜单
	 */
	private List<Map<String, Object>> menus;
	/**
	 * 邮箱校验码
	 */
	private String emailCheckCode;
	/**
	 * 手机校验码
	 */
	private String mobileCheckCode;
	/**
	 * 手机校验码超时时间
	 */
	private Date mobileCheckCodeTimeout;
	/**
	 * 邮箱校验码超时时间
	 */
	private Date emailCheckCodeTimeout;
	/**
	 * 用户商品购物车id
	 */
	private String goodsCartId;
	/**
	 * IP地址
	 */
	private String remoteAddr;

	/**
	 * image
	 */
	private String image;

	/**
	 * 扩展信息
	 */
	private Map<String, Object> self;
	/**
	 * 用户订餐订单校验码
	 */
	private String userOrderCheckCode;
	/**
	 * 用户订餐订单校验码超时时间
	 */
	private Date userOrderCheckCodeTimeout;
	/**
	 * 主机平板获取tokenId
	 */
	private String tokenId;
	/**
	 * 主机平板tokenId超时时间
	 */
	private Date tokenTimeOut;
	/**
	 * 客户端登陆方式
	 */
	private String logonType;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 昵称
	 */
	private String nick;
	/**
	 * 系统流程处理默认用户
	 */
	public static User SYS_USER;

	static {
		SYS_USER = new User();
		SYS_USER.setUserId(ConstantUtil.SYS_USER);
		SYS_USER.setUserName(ConstantUtil.SYS_USER);
		SYS_USER.setUserType(ConstantUtil.USER_TYPE_ADMIN);
	}

	public SessionKey getSessionKey() {

		return sessionKey;
	}

	public void setSessionKey(SessionKey sessionKey) {

		this.sessionKey = sessionKey;
	}

	public boolean isSysUser() {

		return ConstantUtil.SYS_USER.equals(getUserId())
				&& ConstantUtil.SYS_USER.equals(getUserName())
				&& ConstantUtil.USER_TYPE_ADMIN.equals(getUserType());
	}

	public String getMobile() {

		return mobile;
	}

	public void setMobile(String mobile) {

		this.mobile = mobile;
	}

	public String getNick() {

		return nick;
	}

	public void setNick(String nick) {

		this.nick = nick;
	}

	/**
	 *
	 * @return logonType
	 */
	public String getLogonType() {

		return logonType;
	}

	/**
	 *
	 * @param logonType
	 */
	public void setLogonType(String logonType) {

		this.logonType = logonType;
	}

	/**
	 * 客服类型
	 */
	private String serviceClass;

	/**
	 *
	 * @return serviceClass
	 */
	public String getServiceClass() {

		return serviceClass;
	}

	/**
	 *
	 * @param serviceClass
	 */
	public void setServiceClass(String serviceClass) {

		this.serviceClass = serviceClass;
	}

	/**
	 * 地理位置
	 */
	private String position;

	/**
	 * 城市区域
	 */
	private String area;

	public String getArea() {

		return area;
	}

	public void setArea(String area) {

		this.area = area;
	}

	/**
	 *
	 * @return position
	 */
	public String getPosition() {

		return position;
	}

	/**
	 *
	 * @param position
	 */
	public void setPosition(String position) {

		this.position = position;
	}

	/**
	 * 是否持有会话
	 */
	public boolean hasSession() {

		return getUserId() != null;
	}

	/**
	 *
	 * @return self
	 */
	public Map<String, Object> getSelf() {

		return self;
	}

	/**
	 *
	 * @param self
	 */
	public void setSelf(Map<String, Object> self) {

		this.self = self;
	}

	/**
	 *
	 * @return 是否企业账户
	 */
	public boolean isEntAccountUser() {

		return ConstantUtil.USER_TYPE_ENTERPRISE.equals(userType)
				|| ConstantUtil.USER_TYPE_SUB.equals(userType);
	}

	/**
	 *
	 * @return 是否企业超级管理员
	 */
	public boolean isEntAccountSuper() {

		return ConstantUtil.USER_TYPE_ENTERPRISE.equals(userType);
	}

	/**
	 *
	 * @return 是否企业子账户
	 */
	public boolean isEntAccountSub() {

		return ConstantUtil.USER_TYPE_SUB.equals(userType);
	}

	/**
	 *
	 * @return 是否个人普通用户
	 */
	public boolean isPersonUser() {

		return ConstantUtil.USER_TYPE_PERSON.equals(userType);
	}

	/**
	 *
	 * @return 是否客服
	 */
	public boolean isCustomService() {

		return ConstantUtil.USER_TYPE_CUSTOMSERVICE.equals(userType);
	}

	/**
	 *
	 * @return 是否系统管理员
	 */
	public boolean isManagerUser() {

		return ConstantUtil.USER_TYPE_ADMIN.equals(userType);
	}

	/**
	 *
	 * @return hosts
	 */
	public Set<HostAddress> getHosts() {

		return hosts;
	}

	/**
	 *
	 * @param hosts
	 */
	public void setHosts(Set<HostAddress> hosts) {

		this.hosts = hosts;
	}

	/**
	 *
	 * @return emailCheckCodeTimeout
	 */
	public Date getEmailCheckCodeTimeout() {

		return emailCheckCodeTimeout;
	}

	/**
	 *
	 * @param emailCheckCodeTimeout
	 */
	public void setEmailCheckCodeTimeout(Date emailCheckCodeTimeout) {

		this.emailCheckCodeTimeout = emailCheckCodeTimeout;
	}

	/**
	 *
	 * @return image
	 */
	public String getImage() {

		return image;
	}

	/**
	 *
	 * @param image
	 */
	public void setImage(String image) {

		this.image = image;
	}

	/**
	 *
	 * @return remoteAddr
	 */
	public String getRemoteAddr() {

		return remoteAddr;
	}

	/**
	 *
	 * @param remoteAddr
	 */
	public void setRemoteAddr(String remoteAddr) {

		this.remoteAddr = remoteAddr;
	}

	/**
	 *
	 * @return mobileCheckCodeTimeout
	 */
	public Date getMobileCheckCodeTimeout() {

		return mobileCheckCodeTimeout;
	}

	/**
	 *
	 * @param mobileCheckCodeTimeout
	 */
	public void setMobileCheckCodeTimeout(Date mobileCheckCodeTimeout) {

		this.mobileCheckCodeTimeout = mobileCheckCodeTimeout;
	}

	/**
	 *
	 * @return roles
	 */
	public List<String> getRoles() {

		return roles;
	}

	/**
	 *
	 * @param roles
	 */
	public void setRoles(List<String> roles) {

		this.roles = roles;
	}

	/**
	 *
	 * @return goodsCartId
	 */
	public String getGoodsCartId() {

		return goodsCartId;
	}

	/**
	 *
	 * @param goodsCartId
	 */
	public void setGoodsCartId(String goodsCartId) {

		this.goodsCartId = goodsCartId;
	}

	/**
	 *
	 * @return emailCheckCode
	 */
	public String getEmailCheckCode() {

		return emailCheckCode;
	}

	/**
	 *
	 * @param emailCheckCode
	 */
	public void setEmailCheckCode(String emailCheckCode) {

		this.emailCheckCode = emailCheckCode;
	}

	/**
	 *
	 * @return mobileCheckCode
	 */
	public String getMobileCheckCode() {

		return mobileCheckCode;
	}

	/**
	 *
	 * @param mobileCheckCode
	 */
	public void setMobileCheckCode(String mobileCheckCode) {

		this.mobileCheckCode = mobileCheckCode;
	}

	/**
	 *
	 * @return randomCode
	 */
	public String getRandomCode() {

		return randomCode;
	}

	/**
	 *
	 * @param randomCode
	 */
	public void setRandomCode(String randomCode) {

		this.randomCode = randomCode;
	}

	/**
	 *
	 * @return interfaces
	 */
	public Set<String> getInterfaces() {

		return interfaces;
	}

	/**
	 *
	 * @param interfaces
	 */
	public void setInterfaces(Set<String> interfaces) {

		this.interfaces = interfaces;
	}

	/**
	 *
	 * @return menus
	 */
	public List<Map<String, Object>> getMenus() {

		return menus;
	}

	/**
	 *
	 * @param menus
	 */
	public void setMenus(List<Map<String, Object>> menus) {

		this.menus = menus;
	}

	/**
	 *
	 * @return userId
	 */
	public String getUserId() {

		return userId;
	}

	/**
	 *
	 * @param userId
	 */
	public void setUserId(String userId) {

		this.userId = userId;
	}

	/**
	 *
	 * @return userName
	 */
	public String getUserName() {

		return userName;
	}

	/**
	 *
	 * @param userName
	 */
	public void setUserName(String userName) {

		this.userName = userName;
	}

	/**
	 *
	 * @return userType
	 */
	public String getUserType() {

		return userType;
	}

	/**
	 *
	 * @param userType
	 */
	public void setUserType(String userType) {

		this.userType = userType;
	}

	/**
	 *
	 * @return email
	 */
	public String getEmail() {

		return email;
	}

	/**
	 *
	 * @param email
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 *
	 * @return sessionId
	 */
	public String getSessionId() {

		return sessionId;
	}

	/**
	 *
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {

		this.sessionId = sessionId;
	}

	/**
	 *
	 * @return userOrderCheckCode
	 */
	public String getUserOrderCheckCode() {

		return userOrderCheckCode;
	}

	/**
	 *
	 * @param userOrderCheckCode
	 */
	public void setUserOrderCheckCode(String userOrderCheckCode) {

		this.userOrderCheckCode = userOrderCheckCode;
	}

	/**
	 *
	 * @return userOrderCheckCodeTimeout
	 */
	public Date getUserOrderCheckCodeTimeout() {

		return userOrderCheckCodeTimeout;
	}

	/**
	 *
	 * @param userOrderCheckCodeTimeout
	 */
	public void setUserOrderCheckCodeTimeout(Date userOrderCheckCodeTimeout) {

		this.userOrderCheckCodeTimeout = userOrderCheckCodeTimeout;
	}

	/**
	 *
	 * @return tokenId
	 */
	public String getTokenId() {

		return tokenId;
	}

	/**
	 *
	 * @param tokenId
	 */
	public void setTokenId(String tokenId) {

		this.tokenId = tokenId;
	}

	/**
	 *
	 * @return tokenTimeOut
	 */
	public Date getTokenTimeOut() {

		return tokenTimeOut;
	}

	/**
	 *
	 * @param tokenTimeOut
	 */
	public void setTokenTimeOut(Date tokenTimeOut) {

		this.tokenTimeOut = tokenTimeOut;
	}

	/**
	 * 清空用户是否厨师、接口、权限、角色信息
	 */
	public void reset() {

		setInterfaces(null);
		setMenus(null);
		setRoles(null);
	}

	/**
	 * 是否系统超级管理员
	 */
	public boolean isManagerSuper() {

		List<String> roles = getRoles();
		if (roles != null && roles.contains(ConstantUtil.ROLE_ADMIN)) {
			return true;
		} else {
			return false;
		}
	}

}
