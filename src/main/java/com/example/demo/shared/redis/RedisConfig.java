package com.example.demo.shared.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.lettuce.core.RedisClient;


@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    

    @Value("${spring.data.redis.port}")
    private int redisPort;

    public RedisClient redisClient() {
        String redisUrl = String.format("redis://%s:%d", redisHost, redisPort);
        return RedisClient.create(redisUrl);
    }
}
