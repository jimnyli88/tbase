package cn.com.ut.security.jwt.util;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	private String secretKey = "f17adec3910740168c39aff9f883aac7";

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	public void setSecretKey(String secretKey) {

		this.secretKey = secretKey;
	}

	public void setSignatureAlgorithm(SignatureAlgorithm sig) {

		SIGNATURE_ALGORITHM = sig;
	}

	/**
	 * 由字符串生成加密key
	 * 
	 * @return
	 */
	public SecretKey generalKey() {

		byte[] encodedKey = Base64.decodeBase64(secretKey);
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		return key;
	}

	/**
	 * 创建token
	 * 
	 * @param subject
	 * @param claims
	 * @param ttlMillis
	 *            过期时间秒
	 * @return
	 */
	public String createToken(String subject, Map<String, Object> claims, long ttlMillis) {

		JwtBuilder builder = Jwts.builder().setClaims(claims)// .setId(UUID.randomUUID().toString())
				// .setIssuedAt(new Date())
				.setSubject(subject).signWith(SIGNATURE_ALGORITHM, generalKey())// .compressWith(CompressionCodecs.DEFLATE)
				.setExpiration(generateExpirationDate(ttlMillis));
		return builder.compact();
	}

	/**
	 * 获取到期日期
	 * 
	 * @param expiration
	 *            秒
	 * @return
	 */
	private Date generateExpirationDate(long expiration) {

		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * 解密token
	 * 
	 * @param token
	 * @return
	 */
	public Claims parseToken(String token) {

		return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token).getBody();
	}

	/**
	 * 解析token 并获取subject
	 * 
	 * @param token
	 * @return
	 */
	public String getSubject(String token) {

		String subject;
		try {
			final Claims claims = parseToken(token);
			subject = claims.getSubject();
		} catch (Exception e) {
			subject = null;
		}
		return subject;
	}

	/**
	 * 获取token的创建时间
	 * 
	 * @param token
	 * @return
	 */
	public Date getCreatedDateFromToken(String token) {

		Date created;
		try {
			final Claims claims = parseToken(token);
			created = claims.getIssuedAt();
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	/**
	 * 获取token的过期时间
	 * 
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {

		Date expiration;
		try {
			final Claims claims = parseToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

}
