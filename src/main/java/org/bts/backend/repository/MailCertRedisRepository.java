package org.bts.backend.repository;

import org.bts.backend.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MailCertRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public MailCertRedisRepository(@Qualifier("redisTemplate2") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String email, String uuid) {
        redisTemplate.opsForValue().set(email, uuid);
    }

    public Optional<String> findByEmail(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(email));
    }

    public void update(String email) {
        redisTemplate.opsForValue().set(email, "ACK");
    }
    public void delete(String email) {
        redisTemplate.delete(email);
    }
}

