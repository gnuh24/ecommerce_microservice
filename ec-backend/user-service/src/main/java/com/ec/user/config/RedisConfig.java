package com.ec.user.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		
		// Key serializer
		template.setKeySerializer(new StringRedisSerializer());
		
		// Use GenericJackson2JsonRedisSerializer for value and hash value
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
		
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		
		template.afterPropertiesSet();
		return template;
	}
	

}



