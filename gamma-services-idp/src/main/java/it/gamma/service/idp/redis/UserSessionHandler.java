package it.gamma.service.idp.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserSessionHandler
{
	private RedisTemplate<String, String> _redisTemplate;
	
	@Autowired
	public UserSessionHandler(@Qualifier("redis.template") RedisTemplate<String, String> redisTemplate) {
		_redisTemplate = redisTemplate;
	}
	
	public void writeAzCode(String key, String value) {
		_redisTemplate.opsForValue().set("USR_CODE_"+key, value, Duration.ofSeconds(60));
	}
}
