package it.gamma.service.idp.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
	
	@Bean(name="redis.template")
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
	  RedisTemplate<String, String> template = new RedisTemplate<>();
	  template.setConnectionFactory(connectionFactory);
	  template.setKeySerializer(new StringRedisSerializer());
	  template.setHashKeySerializer(new StringRedisSerializer());
	  template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

	  return template;
	}
}
