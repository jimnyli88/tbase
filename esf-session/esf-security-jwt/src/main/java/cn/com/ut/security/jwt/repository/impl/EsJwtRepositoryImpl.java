package cn.com.ut.security.jwt.repository.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.ut.config.jwt.module.JwtConfigModule;
import cn.com.ut.security.jwt.exception.EsfJwtException;
import cn.com.ut.security.jwt.repository.EsJwtRepository;
import cn.com.ut.security.jwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

public class EsJwtRepositoryImpl implements EsJwtRepository {

	private static final Logger logger = LoggerFactory.getLogger(EsJwtRepositoryImpl.class);

	private JwtConfigModule jwtConfigModule;

	private JwtUtil jwtUtil;

	private long defauleAccTime = 3600L;
	private long defauleRefreshTime = 36000L;

	private final String ACCESS = "access";
	private final String REFRESH = "refresh";
	private final String DEFAULT_SUBJECT = "default";
	private final String TOKENTYPE = "tt";
	private final String TT_ACCESS = "access";
	private final String TT_REFRESH = "refresh";

	public EsJwtRepositoryImpl(JwtConfigModule jwtConfigModule, SignatureAlgorithm sig) {
		this.jwtConfigModule = jwtConfigModule;
		jwtUtil = new JwtUtil();
		if (jwtConfigModule == null || jwtConfigModule.getSecretkey() == null
				|| "".equals(jwtConfigModule.getSecretkey().trim())) {
			throw new IllegalArgumentException("secretkey can not be null");
		}
		jwtUtil.setSecretKey(jwtConfigModule.getSecretkey());
		jwtUtil.setSignatureAlgorithm(sig);
		init(jwtConfigModule);
	}

	private void init(JwtConfigModule jwtConfigModule) {

		Map<String, Long> defaultMap = jwtConfigModule.getExpiration().get(DEFAULT_SUBJECT);
		if (defaultMap != null && !defaultMap.isEmpty()) {
			Long tempAccTime = defaultMap.get(ACCESS);
			Long tempRefreshTime = defaultMap.get(REFRESH);
			if (tempAccTime != null) {
				defauleAccTime = tempAccTime;
			}
			if (tempRefreshTime != null) {
				defauleRefreshTime = tempRefreshTime;
			}
		}
	}

	/**
	 * 获取配置文件token有效的时间 如果配置文件没有设置 则返回默认时间
	 * 
	 * @param subject
	 * @param type
	 *            类型 目前有刷新时间 refresh 和 使用时间access两种
	 * @return
	 */
	private long getConfigTime(String subject, String type) {

		if (jwtConfigModule.getExpiration() == null
				|| jwtConfigModule.getExpiration().get(subject) == null
				|| jwtConfigModule.getExpiration().get(subject).get(type) == null) {
			switch (type) {
			case ACCESS:
				return defauleAccTime;
			case REFRESH:
				return defauleRefreshTime;
			default:
				return defauleAccTime;
			}
		}
		return jwtConfigModule.getExpiration().get(subject).get(type);
	}

	@Override
	public long getAccTime(String subject) {

		return getConfigTime(subject, ACCESS);
	}

	@Override
	public long getRefreshTime(String subject) {

		return getConfigTime(subject, REFRESH);
	}

	@Override
	public String createAccessToken(String subject, Map<String, Object> claims) {

		claims.put(TOKENTYPE, TT_ACCESS);
		return jwtUtil.createToken(subject, claims, getAccTime(subject));
	}

	@Override
	public String createRefreshToken(String subject, Map<String, Object> claims) {

		claims.put(TOKENTYPE, TT_REFRESH);
		return jwtUtil.createToken(subject, claims, getRefreshTime(subject));
	}

	@Override
	public String createAccessTokenByRefreshToken(String refreshToken) {

		logger.debug("create createAccessTokenByRefreshToken refreshToken:{}", refreshToken);
		Claims claims = parseToken(refreshToken);
		return createAccessToken(claims.getSubject(), claims);
	}

	@Override
	public String createNewRefreshToken(String refreshToken) {

		logger.debug("create createNewRefreshToken refreshToken:{}", refreshToken);
		Claims claims = parseToken(refreshToken);
		if (jwtConfigModule.getAfterSecondCanRefresh() != null
				&& jwtConfigModule.getAfterSecondCanRefresh() > 0) {
			long minTime = claims.getExpiration().getTime()
					+ jwtConfigModule.getAfterSecondCanRefresh() * 1000;
			long ntime = System.currentTimeMillis() + getRefreshTime(claims.getSubject()) * 1000;
			if (minTime > ntime) {
				throw new EsfJwtException("至少" + (minTime - ntime) / 1000 + " 秒后才使用刷新 ");
			}
		}
		return createRefreshToken(claims.getSubject(), claims);
	}

	@Override
	public Claims parseToken(String token) {

		logger.debug("create parseToken token:{}", token);
		Claims claims = null;
		try {
			claims = jwtUtil.parseToken(token);
		} catch (Exception e) {
			throw new EsfJwtException(" 非法token或者token已经过期  :" + e.getMessage());
		}
		if (claims == null) {
			throw new EsfJwtException(" 非法token或者token已经过期 ");
		}
		return claims;
	}

	@Override
	public Date getCreatedDateFromToken(String token) {

		final Claims claims = parseToken(token);
		Date created = claims.getIssuedAt();
		return created;
	}

	@Override
	public Date getExpirationDateFromToken(String token) {

		final Claims claims = parseToken(token);
		Date created = claims.getExpiration();
		return created;
	}

	@Override
	public boolean isRefreshToken(String token) {

		if (TT_REFRESH.equals(parseToken(token).get(TOKENTYPE))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAccToken(String token) {

		if (TT_ACCESS.equals(parseToken(token).get(TOKENTYPE))) {
			return true;
		}
		return false;
	}
}
