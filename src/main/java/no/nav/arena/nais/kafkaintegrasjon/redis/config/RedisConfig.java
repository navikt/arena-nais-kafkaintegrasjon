package no.nav.arena.nais.kafkaintegrasjon.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import java.util.Arrays;


@Configuration
@PropertySource("classpath:application.yml")
public class RedisConfig {

	@Autowired
	Environment environment;

	@Value("${spring.redis.host}")
	private String redisHostName;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Value("${spring.redis.sentinel.master}")
	private String redisSentinelMaster;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}


	@Bean
	JedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory factory;

		boolean isLocalDev = Arrays.stream(environment.getActiveProfiles()).anyMatch(
				env -> (env.equalsIgnoreCase("test")
						|| env.equalsIgnoreCase("local")) );

		if (isLocalDev) {

			RedisStandaloneConfiguration standaloneConfiguration =
					new RedisStandaloneConfiguration(redisHostName, redisPort);
			factory = new JedisConnectionFactory(
					standaloneConfiguration
			);
		} else {

			RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
					.sentinel(new RedisNode(redisHostName, redisPort));
			if (!redisSentinelMaster.isEmpty()) {
				sentinelConfiguration.setMaster(redisSentinelMaster);
			}

			factory = new JedisConnectionFactory(
					sentinelConfiguration //standaloneConfiguration
			);
		}

		return factory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}
}
