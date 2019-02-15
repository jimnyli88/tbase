package cn.com.ut.config.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableRabbit
@PropertySource({ "classpath:/cn/com/ut/config/rabbitmq/rabbitmq-config.properties" })
public class RabbitMQConfig {

	@Value("broker.addresses")
	private String addresses;
	@Value("broker.username")
	private String username;
	@Value("broker.password")
	private String password;

	@Value("broker.listener.concurrentConsumers")
	private int concurrentConsumers;
	@Value("broker.listener.maxConcurrentConsumers")
	private int maxConcurrentConsumers;

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {

		CachingConnectionFactory cf = new CachingConnectionFactory();
		cf.setAddresses(addresses);
		cf.setUsername(username);
		cf.setPassword(password);
		return cf;
	}

	@Bean
	public RabbitAdmin rabbitAdmin() {

		RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory());
		return rabbitAdmin;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {

		RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
		return rabbitTemplate;

	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {

		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(cachingConnectionFactory());
		factory.setConcurrentConsumers(concurrentConsumers);
		factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
		return factory;
	}

}
