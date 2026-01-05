package com.example.demo.modules.User.infrastructure.security.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long expiresAt) {
        redisTemplate.opsForValue().set(token, "blacklisted");
        redisTemplate.expire(token, Duration.ofSeconds(expiresAt));
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}
