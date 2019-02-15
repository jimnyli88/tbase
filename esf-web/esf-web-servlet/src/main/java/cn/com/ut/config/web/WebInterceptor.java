package cn.com.ut.config.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.com.ut.core.cache.session.SessionManager;
import cn.com.ut.core.common.annotation.ServiceComponent;
import cn.com.ut.core.common.constant.ConstantUtil;
import cn.com.ut.core.common.system.beans.RequestHeader;
import cn.com.ut.core.common.system.beans.User;
import cn.com.ut.core.common.util.CommonUtil;
import cn.com.ut.core.common.util.ExceptionUtil;
import cn.com.ut.core.restful.ResponseWrap;
import cn.com.ut.security.jwt.repository.EsJwtRepository;
import io.jsonwebtoken.Claims;

@Component
public class WebInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(WebInterceptor.class);

	/**
	 * cookie有效时间，单位为秒
	 */
	public static final int COOKIE_TIMEOUT = 60 * 60 * 24 * 7; // 7天

	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private EsJwtRepository esJwtRepository;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		// if it's hessian rpc invoke, the class type of handler is
		// HessianServiceExporter
		if (!(handler instanceof HandlerMethod) || ((HandlerMethod) handler)
				.getBean() instanceof org.springframework.boot.web.servlet.error.ErrorController)
			return true;

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept");

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		User user = loadRequestBasicInfo(request, response);
		ResponseWrap responseWrap = ResponseWrap.builder();

		boolean checkEnable = true;

		if (!handlerMethod.hasMethodAnnotation(ServiceComponent.class)
				|| handlerMethod.getMethodAnnotation(ServiceComponent.class).session()) {
			// 加载会话
			user = sessionManager.loadSessionFromCache(user);

			checkEnable = (user.getUserId() != null && user.getSessionId() != null);
			if (!checkEnable) {
				responseWrap.setCodeMsg(Integer.parseInt(ConstantUtil.RC_SESSION_EXPIRE),
						ConstantUtil.ERR_SESSION_EXPIRE);
				logger.debug(ConstantUtil.ERR_SESSION_EXPIRE);
			}
		}

		logger.debug("==auth=={}", checkEnable);
		printUserInfo(user);
		request.setAttribute("user", user);

		if (!checkEnable) {
			response.setContentType(ConstantUtil.CONTENT_TYPE_JSON);
			byte[] bs = JSON.toJSONBytes(responseWrap.getResponseData(),
					SerializerFeature.WriteNullStringAsEmpty);
			response.setContentLength(bs.length);
			response.getOutputStream().write(bs);
			response.getOutputStream().flush();
		}

		return checkEnable;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		logger.debug("==postHandle==");
	}

	protected User loadRequestBasicInfo(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// request header
		StringBuilder info = new StringBuilder();
		info.append("\nContentType ContentLength==").append(request.getContentType()).append(" ")
				.append(request.getContentLength());
		info.append("\nLocalAddr LocalName LocalPort==").append(request.getLocalAddr()).append(" ")
				.append(request.getLocalName()).append(" ").append(request.getLocalPort());
		info.append("\nRemoteAddr RemoteHost RemotePort==").append(request.getRemoteAddr())
				.append(" ").append(request.getRemoteHost()).append(" ")
				.append(request.getRemotePort());
		info.append("\nRemoteUser==").append(request.getRemoteUser());
		info.append("\nPathInfo PathTranslated==").append(request.getPathInfo()).append(" ")
				.append(request.getPathTranslated());
		info.append("\nRequestURI RequestURL==").append(request.getRequestURI()).append(" ")
				.append(request.getRequestURL());
		info.append("\nScheme==").append(request.getScheme());
		info.append("\nServerName ServerPort==").append(request.getServerName()).append(" ")
				.append(request.getServerPort());

		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setServerName(request.getServerName());
		requestHeader.setServerPort(request.getServerPort());
		requestHeader.setRemoteAddr(request.getRemoteAddr());

		for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements();) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			info.append("\nHeader ").append(name).append("==").append(value);
			if (RequestHeader.REFERER.equals(name)) {
				requestHeader.setReferer(value);
			}
			if (RequestHeader.X_FORWARDED_FOR.equals(name)) {
				requestHeader.setxForwardedFor((value));
			}
			if (RequestHeader.X_REQUESTED_WITH.equals(name)) {
				requestHeader.setxRequestedWith(value);
			}
		}
		logger.debug("request header{}", info.toString());
		User user = loadRequestBasicInfoByToken(request, requestHeader);
		if (user == null) {
			user = loadRequestBasicInfoByCookie(request, response, requestHeader);
		}
		return user;
	}

	private User loadRequestBasicInfoByToken(HttpServletRequest request,
			RequestHeader requestHeader) {

		// 客户端将token封装在请求头中，格式为（Bearer后加空格）：Authorization：Bearer +token
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null) {
			return null;
		}

		User user = new User();

		if (!authHeader.startsWith("Bearer ")) {
			return user;
		}
		// 去除Bearer 后部分
		final String token = authHeader.substring(7);
		if (CommonUtil.isNotEmpty(token)) {
			try {
				Claims tokenP = esJwtRepository.parseToken(token);
				String tokenSessionId = (String) tokenP.get(ConstantUtil.SESSIONKEY);
				if (CommonUtil.isNotEmpty(tokenSessionId)) {
					user.setSessionId(tokenSessionId);
				}
			} catch (Exception e) {
				return user;
			}
			if (!esJwtRepository.isAccToken(token)) {
				ExceptionUtil.throwServiceException("please use accessToken");
			}
		}

		user.setRemoteAddr(requestHeader.getxForwardedFor() == null ? request.getRemoteAddr()
				: requestHeader.getxForwardedFor());
		return user;
	}

	private User loadRequestBasicInfoByCookie(HttpServletRequest request,
			HttpServletResponse response, RequestHeader requestHeader) {

		String sessionKey = null;
		String position = null;
		String area = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				logger.debug(cookie.getName() + "==" + cookie.getValue());
				// 获取会话认证标识
				if (ConstantUtil.SESSIONKEY.equals(cookie.getName())) {
					sessionKey = cookie.getValue();
				}
				// 地理位置坐标点
				if (ConstantUtil.POSITION.equals(cookie.getName())) {
					position = cookie.getValue();
				}
				// 区域地址
				if (ConstantUtil.AREA.equals(cookie.getName())) {
					area = cookie.getValue();
				}
			}
		}

		if (CommonUtil.isEmpty(sessionKey)) {
			// 手机调用接口不会setcookies 通过头获取
			sessionKey = request.getHeader(ConstantUtil.SESSIONKEY);
		}

		User user = new User();
		if (sessionKey == null) {
			sessionKey = CommonUtil.getUUID();
			logger.debug("new sid==" + sessionKey);
			addCookie(request, response, ConstantUtil.SESSIONKEY, sessionKey, null, null, -1);
		}

		user.setRemoteAddr(requestHeader.getxForwardedFor() == null ? request.getRemoteAddr()
				: requestHeader.getxForwardedFor());

		user.setSessionId(sessionKey);

		if (position != null) {
			user.setPosition(new String(Base64.decodeBase64(position)));
		}
		if (area != null) {
			user.setArea(new String(Base64.decodeBase64(area)));
		}

		return user;
	}

	/**
	 * 生成Cookie对象
	 *
	 * @param request
	 *            http请求
	 * @param response
	 *            http响应
	 * @param name
	 *            Cookie键
	 * @param value
	 *            Cookie值
	 * @param path
	 *            路径
	 * @param domain
	 *            域名
	 * @param expiry
	 *            生命期
	 * @return Cookie
	 */
	public Cookie addCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, String path, String domain, int expiry) {

		Cookie cookie = new Cookie(name, value);
		if (path == null)
			cookie.setPath("/");
		// if (domain == null)
		// cookie.setDomain(ConstantUtil.DEFAULT_DOMAIN);
		cookie.setMaxAge(expiry);
		response.addCookie(cookie);
		return cookie;
	}

	public void printUserInfo(User user) {

		StringBuilder info = new StringBuilder();
		info.append("\nsid==").append(user.getSessionId());
		info.append(" uid==").append(user.getUserId());
		info.append(" uname==").append(user.getUserName());
		info.append(" ip==").append(user.getRemoteAddr());
		info.append(" logonway==").append(user.getLogonType());
		logger.debug(info.toString());
	}

}