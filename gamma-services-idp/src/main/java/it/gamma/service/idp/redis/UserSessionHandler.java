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
	
	public void write(ISessionData sessionData, String key, String value) {
		_redisTemplate.opsForValue().set(sessionData.prefix()+key, value, Duration.ofSeconds(sessionData.expireTime()));
	}

	public String readOnce(ISessionData sessionData, String key) {
		String value = _redisTemplate.opsForValue().get(sessionData.prefix()+key);
		_redisTemplate.delete(sessionData.prefix()+key);
		return value;
	}
	
	public String read(ISessionData sessionData, String key) {
		return _redisTemplate.opsForValue().get(sessionData.prefix()+key);
	}
	
	public boolean remove(ISessionData sessionData, String key) {
		return _redisTemplate.delete(sessionData.prefix()+key);
	}
}
