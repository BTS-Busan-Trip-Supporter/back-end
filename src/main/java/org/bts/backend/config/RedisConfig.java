package org.bts.backend.config;

import lombok.RequiredArgsConstructor;
import org.bts.backend.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.redis1.host}")
    public String REDIS_HOST1;
    @Value("${spring.redis2.host}")
    public String REDIS_HOST2;
    @Value("${spring.redis1.port}")
    public int REDIS_PORT1;
    @Value("${spring.redis2.port}")
    public int REDIS_PORT2;

    // 레디스 연결 설정
    @Bean
    public RedisConnectionFactory redisConnectionFactory1() {
        return new LettuceConnectionFactory(REDIS_HOST1, REDIS_PORT2);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory2() {
        return new LettuceConnectionFactory(REDIS_HOST2, REDIS_PORT2);
    }

    // 레디스 데이터 템플릿 설정, Refresh 토큰 저장용으로 String : Object(RedisToken) 형식으로 설정
    @Bean
    // @Qualifier("redisTemplate1")
    public RedisTemplate<String, RefreshToken> redisTemplate1() {
        RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory1());
        return redisTemplate;
    }

    @Bean
    // @Qualifier("redisTemplate2")
    public RedisTemplate<String, String> redisTemplate2() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory2());
        return redisTemplate;
    }
}
