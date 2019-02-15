package cn.com.ut.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.ut.config.jwt.module.JwtConfigModule;
import cn.com.ut.security.jwt.repository.EsJwtRepository;
import cn.com.ut.security.jwt.repository.impl.EsJwtRepositoryImpl;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
@EnableConfigurationProperties(JwtConfigModule.class)
public class JwtConfig {

	@Autowired
	private JwtConfigModule jwtConfigModule;

	@Bean
	public EsJwtRepository esJwtRepository() {

		EsJwtRepository esJwtRepository = new EsJwtRepositoryImpl(jwtConfigModule,
				SignatureAlgorithm.HS256);
		return esJwtRepository;
	}

}
