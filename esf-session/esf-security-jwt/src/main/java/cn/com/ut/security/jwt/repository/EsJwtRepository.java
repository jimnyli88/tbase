package cn.com.ut.security.jwt.repository;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;

public interface EsJwtRepository {

	/**
	 * 创建一个token
	 * 
	 * @param subject
	 *            类型 配合配置文件esf.security.jwt.expiration.app.access app就是该类型
	 * @param claims
	 *            内容体
	 * @return
	 */
	String createAccessToken(String subject, Map<String, Object> claims);

	/**
	 * 创建一个刷新专用的token
	 * 
	 * @param subject
	 *            类型 配合配置文件esf.security.jwt.expiration.app.access app就是该类型
	 * @param claims
	 *            内容体
	 * @return
	 */
	String createRefreshToken(String subject, Map<String, Object> claims);

	/**
	 * 根据原有的token创建一个新的token 重置过期时间
	 * 
	 * @param refreshToken
	 * @return
	 */
	String createAccessTokenByRefreshToken(String refreshToken);

	/**
	 * 根据刷新的token 重新获取一个刷新token 重置过期时间
	 * 
	 * @param refreshToken
	 * @return
	 */
	String createNewRefreshToken(String refreshToken);

	/**
	 * 解密token
	 * 
	 * @param token
	 * @return
	 */
	Claims parseToken(String token);

	/**
	 * 获取token的创建时间
	 * 
	 * @param token
	 * @return
	 */
	Date getCreatedDateFromToken(String token);

	/**
	 * 获取token的过期时间
	 * 
	 * @param token
	 * @return
	 */
	Date getExpirationDateFromToken(String token);

	/**
	 * 获取配置文件中的token使用时间
	 * 
	 * @param subject
	 * @return
	 */
	long getAccTime(String subject);

	/**
	 * 获取配置文件中的token刷新时间
	 * 
	 * @param subject
	 * @return
	 */
	long getRefreshTime(String subject);

	/**
	 * 是不是刷新专用token
	 * 
	 * @param token
	 * @return
	 */
	boolean isRefreshToken(String token);

	/**
	 * 是不是Acc token
	 * 
	 * @param token
	 * @return
	 */
	boolean isAccToken(String token);

}
